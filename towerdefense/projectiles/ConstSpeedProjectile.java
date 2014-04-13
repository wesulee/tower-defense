package towerdefense.projectiles;

import towerdefense.util.Utility;

abstract public class ConstSpeedProjectile extends TimedMovingProjectile
{	
	// speed in pixels per second
	public ConstSpeedProjectile(int sourceX, int sourceY,
			int destX, int destY, int speed) {
		super(sourceX, sourceY, destX, destY,
				(int)(Utility.length(destX - sourceX, destY - sourceY)
						/ speed * 1000));
	}
}
