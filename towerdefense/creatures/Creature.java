package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import towerdefense.Direction;
import towerdefense.DirectionVector;

public abstract class Creature 
{
	protected Direction dir = Direction.N;
	private DirectionVector dirVect;
	protected double posX;
	protected double posY;
	private int health;
	private final int speed;
	private final int goldDrop;
	protected final Rectangle rect1;	// N, S
	protected final Rectangle rect2;	// E, W
	private double speedMult = 1.0;
	private int pathIndex = 0;
	protected final int sizeX;
	protected final int sizeY;
	
	public Creature(int health, int speed, int goldDrop, double posX,
			double posY, int sizeX, int sizeY)
	{
		this.health = health;
		this.speed = speed;
		this.goldDrop = goldDrop;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.rect1 = new Rectangle();
		this.rect1.width = sizeX;
		this.rect1.height = sizeY;
		this.rect2 = new Rectangle();
		this.rect2.width = sizeY;
		this.rect2.height = sizeX;
		setPosition(posX, posY);
	}
	
	public int getHealth() {return health;}
	public boolean isAlive() {return health > 0;}
	public void decreaseHealth(int amount) {health -= amount;}
	public int getSpeed() {return speed;}
	public int getGoldDrop() {return goldDrop;}
	
	public Rectangle getRectangle()
	{
		Rectangle rect;
		switch (dir) {
		case N:
		case S:
			rect = rect1;
			break;
		case E:
		case W:
			rect = rect2;
			break;
		default:
			rect = null;	// shouldn't ever happen
		}
		return rect;
	}
	
	public double getPositionX() {return posX;}
	public double getPositionY() {return posY;}
	
	public void setPosition(double x, double y)
	{
		posX = x;
		posY = y;
		switch (dir) {
		case N:
		case S:
			rect1.x = (int)x - sizeX / 2;
			rect1.y = (int)y - sizeY / 2;
		case E:
		case W:
			rect2.x = (int)x - sizeY / 2;
			rect2.y = (int)y - sizeX / 2;
		}
		
	}
	
	public int getPathIndex() {return pathIndex;}
	public void setPathIndex(int i) {pathIndex = i;}
	public void setSpeedMult(double v) {speedMult = v;}
	public double getSpeedMult() {return speedMult;}
	public DirectionVector getDirVect() {return dirVect;}
	
	public void setDirVect(DirectionVector dv)
	{
		dirVect = dv;
		dir = dirVect.toDirection();
	}
	
	
	public abstract void draw(Graphics2D g);
	public abstract CreatureType getType();
	
	
	public static Creature newCreature(CreatureType ct, double x, double y)
	{
		Creature nc;
		switch (ct) {
		case Spider:
			nc = new CreatureSpider(x, y);
			break;
		default:
			nc = null;
		}
		
		return nc;
	}
}
