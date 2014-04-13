package towerdefense.projectiles;

import towerdefense.util.Counter;

/**
 * General projectile that travels from source to fixed destination
 * in a given amount of milliseconds.
 * Subclasses should implement draw(), override/extend update(),
 * and effect of impact.
 */
public abstract class TimedMovingProjectile
{
	private final Counter counter;
	// current position
	protected double x;
	protected double y;
	// calling update moves the projectile by this amount
	protected final double dx;
	protected final double dy;
	
	public TimedMovingProjectile(int sourceX, int sourceY,
			int destX, int destY, int ms)
	{
		this.counter = new Counter(ms);
		this.x = sourceX;
		this.y = sourceY;
		this.dx = (double) (destX - sourceX) / counter.getMaxTicks();
		this.dy = (double) (destY - sourceY) / counter.getMaxTicks();
	}

	// returns true when projectile should be destroyed
	public boolean update()
	{
		if (counter.update())
			return true;
		else {
			x += dx;
			y += dy;
			return false;
		}
	}
	
	protected double getX() {return x;}
	protected double getY() {return y;}
}
