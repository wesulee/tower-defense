package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import towerdefense.animation.AnimationContainer;
import towerdefense.animation.CreatureGoldDrop;
import towerdefense.components.Player;
import towerdefense.creatures.effects.CreatureEffectSource;
import towerdefense.creatures.effects.EffectContainer;
import towerdefense.gamestates.RunningGame;
import towerdefense.maps.GameMap;
import towerdefense.util.DirectionVector;
import towerdefense.util.Utility;

public class CreatureContainer
{
	private final LinkedList<Creature> creatures;
	private LinkedList<Creature> spawnQueue;
	private final LinkedHashMap<Creature, EffectContainer> effects;
	// most recent creature spawned
	private Creature spawnCreature = null;
	private final Rectangle spawnRect;
	private final int END_INDEX;
	// used to implement creature's speed
	private final double delayTime;
	
	private final RunningGame rg;
	private final GameMap map;
	private final Player player;
	private final AnimationContainer ac;
	
	public CreatureContainer(RunningGame rg)
	{
		Creature.cc = this;
		this.rg = rg;
		this.map = rg.getMap();
		this.player = rg.getPlayer();
		this.ac = new AnimationContainer();
		this.spawnRect = map.getSpawnRectangle();
		this.creatures = new LinkedList<Creature>();
		this.effects = new LinkedHashMap<Creature, EffectContainer>();
		this.spawnQueue = new LinkedList<Creature>();
		this.END_INDEX = map.getPointsLength() - 1;
		this.delayTime = (double) rg.getGamePanel().getPeriod() / 1e9;
	}
	
	public void draw(Graphics2D g)
	{
		for (int i = 0; i < creatures.size(); i++)
			creatures.get(i).draw(g);
		
		ac.draw(g);
	}

	public void update()
	{
		// update creature effects
		Iterator<Map.Entry<Creature, EffectContainer>> it =
				effects.entrySet().iterator();
		while (it.hasNext()) {
			if (it.next().getValue().update())
				it.remove();
		}
		
		// try to spawn creature
		Creature c;
		if (!spawnQueue.isEmpty()) {
			// no creature spawned yet, spawn immediately
			if (spawnCreature == null) {
				addNewCreature(spawnQueue.remove(0));
			}
			// no creatures alive, spawn immediately
			else if (creatures.isEmpty()) {
				addNewCreature(spawnQueue.remove(0));
			}
			// spawn if last spawned is dead or outside spawn rect
			else if (!spawnCreature.isAlive() ||
					!spawnCreature.getRectangle().intersects(spawnRect)) {
				addNewCreature(spawnQueue.remove(0));
			}
		}
		
		// remove dead creatures and update positions
		if (!creatures.isEmpty()) {
			Iterator<Creature> iter = creatures.iterator();
			while (iter.hasNext()) {
				c = iter.next();
				if (!c.isAlive()) {
					ac.add(new CreatureGoldDrop(c));
					player.increaseGold(c.getGoldDrop());
					iter.remove();
					effects.remove(c);
				}
				else if (updatePosition(c))
					iter.remove();
			}
		}
		else if (spawnQueue.isEmpty())
			rg.getWaveController().notifyWaveFinished();
		
		ac.update();
	}
	
	// returns true when creature reaches goal
	private boolean updatePosition(final Creature c)
	{
		final int pathIndex = c.getPathIndex();
		final DirectionVector dirVect = c.getDirVect();
		final double creatureX = c.getPositionX();
		final double creatureY = c.getPositionY();
		final double dMult = c.getSpeed() * c.getSpeedMult() * delayTime;
		double dx = dirVect.getX() * dMult;
		double dy = dirVect.getY() * dMult;
		double newX = creatureX + dx;
		double newY = creatureY + dy;
		
		if ((int)newX == creatureX && (int)newY == creatureY) {
			c.setPosition(newX, newY);
		}
		else {
			final Rectangle newRect = new Rectangle(c.getRectangle());
			newRect.x = (int)newX - newRect.width / 2;
			newRect.y = (int)newY - newRect.height / 2;
			if (!intersectsOtherCreatures(c, newRect))
				c.setPosition(newX, newY);
			else
				return false;
		}
		
		if (c.closeEnough()) {
			if (pathIndex == END_INDEX) {
				player.decreaseHealth(1);
				return true;
			}
			else {
				updateCreature(c);
			}
		}
		
		return false;
	}
	
	private void updateDirVect(final Creature c)
	{
		final int pathIndex = c.getPathIndex();
		c.setDirVect(new DirectionVector(
				map.getPathX(pathIndex) - c.getPositionX(),
				map.getPathY(pathIndex) - c.getPositionY()
		));
	}
	
	public void setSpawnQueue(LinkedList<Creature> spawn)
	{
		spawnQueue = spawn;
	}
	
	// no other class should modify size of creatures
	public LinkedList<Creature> getCreatureList() {return creatures;}
	
	public LinkedList<Creature> getCreaturesNear(final int x, final int y,
			final int range)
	{
		LinkedList<Creature> inRange = new LinkedList<Creature>();
		
		for (Creature c : creatures) {
			if (getDistanceFrom(x, y, c) <= range)
				inRange.add(c);
		}
		
		return inRange;
	}
	
	public void applyCreatureEffect(Creature c, CreatureEffectSource source)
	{
		EffectContainer ec = effects.get(c);
		if (ec == null) {
			ec = new EffectContainer(c, source);
			effects.put(c, ec);
		}
		else {
			ec.addEffect(source);
		}
	}
	
	public Creature getCreatureAt(final int x, final int y)
	{
		for (Creature c : creatures) {
			if (c.getRectangle().contains(x, y))
				return c;
		}
		return null;
	}
	
	private double getDistanceFrom(int x, int y, Creature c)
	{
		return Utility.length(
				x - (int)c.getPositionX(),
				y - (int)c.getPositionY()
		);
	}
	
	private boolean intersectsOtherCreatures(Creature c, Rectangle cRect)
	{
		for (Creature test : creatures) {
			if (c != test && cRect.intersects(test.getRectangle()))
				return true;
		}
		return false;
	}
	
	// spawn the creature
	private void addNewCreature(final Creature c)
	{
		spawnCreature = c;
		updateDirVect(c);
		c.updatePathRect(map.getPathX(0), map.getPathY(0),
				map.getPathDistance(0));
		creatures.add(c);
	}
	
	// creature has made it to current checkpoint, update to next one
	private void updateCreature(final Creature c)
	{
		final int pathIndex = c.getPathIndex() + 1;
		c.setPathIndex(pathIndex);
		updateDirVect(c);
		c.updatePathRect(map.getPathX(pathIndex), map.getPathY(pathIndex),
				map.getPathDistance(pathIndex));
	}
}
