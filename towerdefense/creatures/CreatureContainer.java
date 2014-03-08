package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import towerdefense.Direction;
import towerdefense.GameMap;
import towerdefense.Player;
import towerdefense.gamestates.RunningGame;

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
	
	private final RunningGame rg;
	private final GameMap map;
	private final Player player;
	
	public CreatureContainer(RunningGame rg)
	{
		this.rg = rg;
		this.map = rg.getMap();
		this.player = rg.getPlayer();
		this.spawnRect = map.getSpawnRectangle();
		
		creatures = new ArrayList<Creature>();
		spawnQueue = new ArrayList<Creature>();
		END_INDEX = map.getPointsLength() - 1;
		
		delayTime = (double)rg.getGamePanel().getPeriod() / 1000000000.0;
	}

	public void update()
	{
		Creature c;
		// try to spawn creature
		if (!spawnQueue.isEmpty()) {
			if (spawnCreature == null)
				spawnCreature = spawnQueue.get(0);
			
			if (creatures.isEmpty())
				creatures.add(spawnQueue.remove(0));
			else {
				c = creatures.get(creatures.size() - 1);
				if (!c.getRectangle().intersects(spawnRect))
					creatures.add(spawnQueue.remove(0));
			}
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
				if (updatePosition(c)) {
					creatures.remove(i);
					i--;
				}
			}
		}
		
		if (creatures.isEmpty() && spawnQueue.isEmpty())
			rg.getWaveController().notifyWaveFinished();
	}
	
	// returns true when creature reaches goal
	private boolean updatePosition(Creature c)
	{
		int pathIndex = c.getPathIndex();
		double creatureX = c.getPositionX();
		double creatureY = c.getPositionY();
		double dirX = map.getPathX(pathIndex) - creatureX;
		double dirY = map.getPathY(pathIndex) - creatureY;
		double dirLen = Math.sqrt(dirX*dirX + dirY*dirY);
		double dx = c.getSpeed() * dirX / dirLen * delayTime;
		double dy = c.getSpeed() * dirY / dirLen * delayTime;
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
			}
		}
		
		return false;
	}
	
	private boolean closeEnough(double x1, double y1, double x2, double y2,
			int maxDistance)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		return (Math.sqrt(dx*dx + dy*dy) <= maxDistance);
	}
	
	private Direction getDirection(double x1, double x2, double y1, double y2)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		if (dx == 0)
			return dy >= 0 ? Direction.N : Direction.S;
		if (dy == 0)
			return dx >= 0 ? Direction.E : Direction.W;
		
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
		int dx = x - (int)c.getPositionX();
		int dy = y - (int)c.getPositionY();
		return Math.sqrt(dx*dx + dy*dy);
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
