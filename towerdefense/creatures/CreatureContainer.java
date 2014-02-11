package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import towerdefense.Direction;
import towerdefense.GamePanel;
import towerdefense.GameMap;
import towerdefense.Player;

public class CreatureContainer
{
	private ArrayList<Creature> creatures;
	private ArrayList<Creature> spawnQueue;
	// most recent creature spawned
	private Creature spawnCreature = null;
	private final Rectangle spawnRect;
	private final int END_INDEX;
	// used to implement creature's speed
	private final double delayTime;
	
	private final GamePanel gp;
	private final GameMap map;
	private final Player player;
	
	public CreatureContainer(GamePanel gp)
	{
		this.gp = gp;
		this.map = gp.getMap();
		this.player = gp.getPlayer();
		this.spawnRect = map.getSpawnRectangle();
		
		creatures = new ArrayList<Creature>();
		spawnQueue = new ArrayList<Creature>();
		END_INDEX = map.getPointsLength() - 1;
		
		delayTime = (double) gp.getRedrawDelay() / 1000000000.0;
	}

	public void update()
	{
		Creature c;
		// try to spawn creature
		if (!spawnQueue.isEmpty() && ((spawnCreature == null) ||
				!spawnCreature.getRectangle().intersects(spawnRect))) {
			System.out.println("Creature spawned");
			c = spawnQueue.remove(0);
			creatures.add(c);
		}
		
		// remove dead creatures, update positions
		for (int i = 0; i < creatures.size(); i++) {
			c = creatures.get(i);
			// is creature dead?
			if (c.getHealth() <= 0) {
				player.increaseGold(c.getGoldDrop());
				creatures.remove(i);
				i--;
			}
			else {
				updatePosition(c);
			}
		}
		
		if (creatures.isEmpty() && spawnQueue.isEmpty())
			gp.getWaveController().notifyWaveFinished();
	}
	
	private void updatePosition(Creature c)
	{
		double creatureX = c.getPositionX();
		double creatureY = c.getPositionY();
		double dirX = map.getPathX(c.getPathIndex()) - creatureX;
		double dirY = map.getPathY(c.getPathIndex()) - creatureY;
		double dirLen = Math.sqrt(dirX*dirX + dirY*dirY);
		double dx = c.getSpeed() * dirX / dirLen * delayTime;
		double dy = c.getSpeed() * dirY / dirLen * delayTime;
		
		// incomplete implementation
		c.setPosition(creatureX + dx, creatureY + dy);
	}
	
	private boolean closeEnough(double x1, double y1, double x2, double y2,
			int maxDistance)
	{
		return (Math.sqrt(x2 - x1 + y2 - y1) < maxDistance);
	}
	
	private Direction getDirection(double x1, double x2, double y1, double y2)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		if (dx == 0) {
			if (dy >= 0)
				return Direction.N;
			else
				return Direction.S;
		}
		if (dy == 0) {
			if (dx >= 0)
				return Direction.E;
			else
				return Direction.W;
		}
		
		if (Math.abs(dx) > Math.abs(dy))
			return dx >= 0 ? Direction.E : Direction.W;
		else
			return dy >= 0 ? Direction.N : Direction.S;
	}
	
	public void draw(Graphics2D g)
	{
		for (int i = 0; i < creatures.size(); i++) {
			Creature c = creatures.get(i);
			c.draw(g);
		}
	}
	
	public void setSpawnQueue(ArrayList<Creature> spawn)
	{
		spawnQueue = spawn;
	}
	
	// no other class should modify size of creatures
	public ArrayList<Creature> getCreatureList() {return creatures;}
}
