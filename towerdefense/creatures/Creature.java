package towerdefense.creatures;

import java.awt.Rectangle;


/**
 * Speed is distance that creature should travel in 1 sec
 */
public abstract class Creature 
{
	private int health;
	private final int maxHealth;
	private final int speed;
	private final int goldDrop;
	protected double pos_x;
	protected double pos_y;
	protected Rectangle rect;
	
	public Creature(int health, int speed, int goldDrop, double pos_x, 
			double pos_y) {
		this.health = health;
		this.maxHealth = health;
		this.speed = speed;
		this.goldDrop = goldDrop;
		this.rect = new Rectangle();
	}
	
	public abstract void setPosition(double x, double y);
	
	public double getPositionX() {return pos_x;}
	public double getPositionY() {return pos_y;}
	public int getHealth() {return health;}
	public int getGoldDrop() {return goldDrop;}
	public void decreaseHealth(int amount) {health -= amount;}
	public abstract CreatureType getType();
	public Rectangle getRectangle() {return rect;}
	
	public static Creature newCreature(CreatureType ct, double x, double y)
	{
		Creature nc;
		switch (ct) {
		case TestCreatureType:
			nc = new TestCreature(x, y);
			break;
		default:
			nc = null;
		}
		
		return nc;
	}
}
