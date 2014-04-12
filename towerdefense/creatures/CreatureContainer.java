package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;

import towerdefense.DirectionVector;
import towerdefense.Player;
import towerdefense.Utility;
import towerdefense.animation.AnimationContainer;
import towerdefense.animation.CreatureGoldDrop;
import towerdefense.gamestates.RunningGame;
import towerdefense.maps.GameMap;

public class CreatureContainer
{
	private LinkedList<Creature> creatures;
	private LinkedList<Creature> spawnQueue;
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
		this.rg = rg;
		this.map = rg.getMap();
		this.player = rg.getPlayer();
		this.ac = new AnimationContainer();
		this.spawnRect = map.getSpawnRectangle();
		
		creatures = new LinkedList<Creature>();
		spawnQueue = new LinkedList<Creature>();
		END_INDEX = map.getPointsLength() - 1;
		
		delayTime = (double)rg.getGamePanel().getPeriod() / 1000000000.0;
	}
	
	public void draw(Graphics2D g)
	{
		for (int i = 0; i < creatures.size(); i++)
			creatures.get(i).draw(g);
		
		ac.draw(g);
	}

	public void update()
	{
		Creature c;
		// try to spawn creature
		if (!spawnQueue.isEmpty()) {
			// no creature spawned yet, spawn immediately
			if (spawnCreature == null) {
				spawnCreature = spawnQueue.remove(0);
				updateDirVect(spawnCreature);
				creatures.add(spawnCreature);
			}
			// no creatures alive, spawn immediately
			else if (creatures.isEmpty()) {
				spawnCreature = spawnQueue.remove(0);
				updateDirVect(spawnCreature);
				creatures.add(spawnCreature);
			}
			// spawn if last spawned is dead or outside spawn rect
			else if (!spawnCreature.isAlive() ||
					!spawnCreature.getRectangle().intersects(spawnRect)) {
				spawnCreature = spawnQueue.remove(0);
				updateDirVect(spawnCreature);
				creatures.add(spawnCreature);
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
	private boolean updatePosition(Creature c)
	{
		int pathIndex = c.getPathIndex();
		DirectionVector dirVect = c.getDirVect();
		double creatureX = c.getPositionX();
		double creatureY = c.getPositionY();
		double dMult = c.getSpeed() * c.getSpeedMult() * delayTime;
		double dx = dirVect.getX() * dMult;
		double dy = dirVect.getY() * dMult;
		double newX = creatureX + dx;
		double newY = creatureY + dy;
		
		if ((int)newX == creatureX && (int)newY == creatureY) {
			c.setPosition(newX, newY);
		}
		else {
			Rectangle newRect = new Rectangle(c.getRectangle());
			newRect.x = (int)newX - c.getType().getSizeX() / 2;
			newRect.y = (int)newY - c.getType().getSizeY() / 2;
			if (!intersectsOtherCreatures(c, newRect)) {
				c.setPosition(newX, newY);
			}
			else
				return false;
		}
		
		if (closeEnough(
				map.getPathX(pathIndex),
				map.getPathY(pathIndex),
				c.getPositionX(),
				c.getPositionY(),
				map.getPathDistance(pathIndex))) {
			if (pathIndex == END_INDEX) {
				player.decreaseHealth(1);
				return true;
			}
			else {
				c.setPathIndex(pathIndex + 1);
				updateDirVect(c);
			}
		}
		
		return false;
	}
	
	private boolean closeEnough(double x1, double y1, double x2, double y2,
			int maxDistance)
	{
		return (Utility.length(x2 - x1, y2 - y1) <= maxDistance);
	}
	
	private void updateDirVect(Creature c)
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
	
	public LinkedList<Creature> getCreaturesNear(int x, int y, int range)
	{
		LinkedList<Creature> inRange = new LinkedList<Creature>();
		
		for (Creature c : creatures) {
			if (getDistanceFrom(x, y, c) <= range)
				inRange.add(c);
		}
		
		return inRange;
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
			if (c != test && cRect.intersects(test.getRectangle())) {
				return true;
			}
		}
		return false;
	}
}
