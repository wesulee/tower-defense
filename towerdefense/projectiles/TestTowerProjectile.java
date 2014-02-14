package towerdefense.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import towerdefense.creatures.Creature;

/**
 * 
 */
public class TestTowerProjectile extends TimedMovingProjectile implements Projectile
{
	private static final int beamWidth = 8;
	private static final int beamHeight = 3;
	private static final Color beamColor = Color.red;
	private static final int milliseconds = 100;
	private final Path2D.Double beam;
	private final AffineTransform at;
	
	public TestTowerProjectile(int sourceX, int sourceY, Creature target)
	{
		super(sourceX, sourceY, (int)target.getPositionX(),
				(int)target.getPositionY(), milliseconds);
		Rectangle beamRect = new Rectangle(sourceX - beamWidth / 2,
				sourceY - beamHeight / 2, beamWidth, beamHeight);
		double angle = Math.atan(super.dy / super.dx);
		beam = new Path2D.Double();
		beam.append(beamRect, false);
		at = new AffineTransform();
		at.rotate(angle);
		beam.transform(at);
	}

	public void draw(Graphics2D g) {
		Color oldColor = g.getColor();
		g.setColor(beamColor);
		g.draw(beam);
		g.setColor(oldColor);
	}
	
	public boolean update()
	{
		boolean retBool = super.update();
		
		at.setToTranslation(super.dx, super.dy);
		beam.transform(at);
		
		return retBool;
	}
}
