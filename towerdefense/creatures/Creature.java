package towerdefense.creatures;

public class Creature 
{
	private int health;
	private final int maxHealth;
	private final int speed;
	private double pos_x;
	private double pos_y;
	private final int goldDrop;
	
	public Creature(int health, int speed, int goldDrop, double pos_x, 
			double pos_y) {
		this.health = health;
		this.maxHealth = health;
		this.speed = speed;
		this.goldDrop = goldDrop;
	}
	
	public void setPosition(double x, double y)
	{
		pos_x = x;
		pos_y = y;
	}
	
	public double getPositionX() {return pos_x;}
	public double getPositionY() {return pos_y;}
}
