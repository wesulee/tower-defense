package towerdefense.towers;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

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
	
	private static final Color rangeCircleColor = 
			new Color(217, 230, 255);
	private static final Color rangeCircleStrokeColor = 
			new Color(110, 161, 255);
	private static final float rangeCircleAlpha = 0.3f;
	private static final BasicStroke rangeCircleStroke = 
			new BasicStroke(2.0f);
	
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
	
	public void drawRangeCircle(Graphics2D g)
	{
		drawRangeCircleAt(g, pos_x, pos_y, range);
	}
	
	private static void drawRangeCircleAt(Graphics2D g, int x, int y, int range)
	{
		Color oldColor = g.getColor();
		Composite oldComposite = g.getComposite();
		Stroke oldStroke = g.getStroke();
		
		g.setColor(rangeCircleColor);
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, rangeCircleAlpha));
		g.setStroke(rangeCircleStroke);
		g.fillOval(x - range, y - range, range * 2, range * 2);
		g.setColor(rangeCircleStrokeColor);
		g.drawOval(x - range, y - range, range * 2, range * 2);
		
		g.setColor(oldColor);
		g.setComposite(oldComposite);
		g.setStroke(oldStroke);
	}
	
	public static void drawRangeCircleWithTower(Graphics2D g,
			BufferedImage img, int x, int y, int range)
	{
		drawRangeCircleAt(g, x, y, range);
		g.drawImage(img, x - (img.getWidth() / 2),
				y - (img.getHeight() / 2), null);
	}
	
	public static Tower newTower(TowerType tt, int x, int y)
	{
		Tower nt;
		switch (tt) {
		case TestTowerType:
			nt = new TestTower(x, y);
			break;
		case TestTower2Type:
			nt = new TestTower2(x, y);
			break;
		default:
			nt = null;
		}
		
		return nt;
	}
}