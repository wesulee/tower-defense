package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Creature 
{
	private int health;
	private final int speed;
	private final int goldDrop;
	protected double pos_x;
	protected double pos_y;
	protected Rectangle rect;
	private int pathIndex = 0;
	private final HealthBar healthBar;
	
	public Creature(int health, int speed, int goldDrop, double pos_x, 
			double pos_y) {
		this.health = health;
		this.speed = speed;
		this.goldDrop = goldDrop;
		this.rect = new Rectangle();
		this.healthBar = new HealthBar(this);
	}
	
	public abstract void setPosition(double x, double y);
	public double getPositionX() {return pos_x;}
	public double getPositionY() {return pos_y;}
	public int getHealth() {return health;}
	public int getSpeed() {return speed;}
	public int getGoldDrop() {return goldDrop;}
	public void decreaseHealth(int amount)
	{
		health -= amount;
		healthBar.update();
	}
	public abstract CreatureType getType();
	public abstract void draw(Graphics2D g);
	public void drawHealthBar(Graphics2D g) {healthBar.draw(g);}
	public Rectangle getRectangle() {return rect;}
	public int getPathIndex() {return pathIndex;}
	public void setPathIndex(int i) {pathIndex = i;}
	
	
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
