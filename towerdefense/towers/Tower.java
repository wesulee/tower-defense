package towerdefense.towers;

import java.awt.Graphics2D;

public abstract class Tower
{
	private double damage;		// damage per attack
	private int range;			// distance tower can hit target
	private double speed;		// attacks per second
	private int size;			// radius of tower
	private final int cost;		// purchase cost
	private long lastAttack;
	private long attackPeriod;	// delay between attacks, ns
	private final int pos_x;	// tower position, x
	private final int pos_y;	// tower position, y
	
	public Tower(double damage, int range, double speed, int size,
			int cost, int pos_x, int pos_y)
	{
		this.damage = damage;
		this.range = range;
		this.speed = speed;
		this.size = size;
		this.cost = cost;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		attackPeriod = (long)(1000.0 / speed) * 1000000L;
		// tower can attack immediately after created
		lastAttack = System.nanoTime() - attackPeriod;
	}
	
	public boolean canAttack(long time)
	{
		return time - lastAttack > attackPeriod;
	}
	
	public double getDamage() {return damage;}
	public int getRange() {return range;}
	public void setRange(int range) {this.range = range;}
	public double getSpeed() {return speed;}
	public void setSpeed(double speed) {this.speed = speed;}
	public int getSize() {return size;}
	public int getCost() {return cost;}
	public long getAttackPeriod() {return attackPeriod;}
	public void setAttackPeriod(long p) {attackPeriod = p;}
	public void setLastAttack(long time) {lastAttack = time;}
	public int getX() {return pos_x;}
	public int getY() {return pos_y;}
	public abstract void draw(Graphics2D g);
	public abstract TowerType getTowerType();
}