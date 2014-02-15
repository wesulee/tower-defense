package towerdefense.projectiles;

import towerdefense.GamePanel;

/**
 * General projectile that travels from source to fixed destination
 * in a given amount of milliseconds.
 * Subclasses should implement draw(), override/extend update(),
 * and effect of impact.
 */
public abstract class TimedMovingProjectile
{
	private static final long updateElapse = GamePanel.period;
	// how many times update should have been called before destroyed
	private final int maxUpdateTicks;
	// number of calls to update()
	private int updateTickCounter = 0;
	// current position
	protected double x;
	protected double y;
	// calling update moves the projectile by this amount
	protected double dx;
	protected double dy;
	
	public TimedMovingProjectile(int sourceX, int sourceY,
			int destX, int destY, int milliseconds)
	{
		long milliToNano = milliseconds * 1000000L;
		this.maxUpdateTicks = (int)(milliToNano / updateElapse) + 1;
		
		this.x = sourceX;
		this.y = sourceY;
		this.dx = (double)(destX - sourceX) / maxUpdateTicks;
		this.dy = (double)(destY - sourceY) / maxUpdateTicks;
	}

	// returns true when projectile should be destroyed
	public boolean update()
	{
		if (updateTickCounter >= maxUpdateTicks)
			return true;
		x += dx;
		y += dy;
		updateTickCounter++;
		return false;
	}

}
