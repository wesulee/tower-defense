package towerdefense.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import towerdefense.creatures.Creature;

/**
 * Deals fixed damage to target on impact.
 */
public class TestTowerProjectile extends TimedMovingProjectile implements Projectile
{
	private static final int beamDamage = 4;
	private static final int beamWidth = 12;
	private static final int beamHeight = 5;
	private static final Color beamColor = Color.red;
	private static final int milliseconds = 100;
	private final Path2D.Double beam;
	private final AffineTransform at;
	
	private final Creature target;
	
	public TestTowerProjectile(int sourceX, int sourceY, Creature target)
	{
		super(sourceX, sourceY, (int)target.getPositionX(),
				(int)target.getPositionY(), milliseconds);
		this.target = target;
		this.at = new AffineTransform();
		this.beam = new Path2D.Double();
		
		// create beam rectangle and rotate it
		final Rectangle beamRect = new Rectangle(sourceX - beamWidth / 2,
				sourceY - beamHeight / 2, beamWidth, beamHeight);
		double angle = Math.atan(dy / dx);
		AffineTransform t = new AffineTransform();
		
		beam.append(beamRect, false);
		t.rotate(angle, beamRect.x + beamRect.width / 2,
				beamRect.y + beamRect.height / 2);
		beam.transform(t);
	}

	public void draw(Graphics2D g)
	{
		final Color oldColor = g.getColor();
		
		g.setColor(beamColor);
		g.fill(beam);
		
		g.setColor(oldColor);
	}
	
	public boolean update()
	{
		final boolean retBool = super.update();
		
		// update position of drawn beam
		at.setToTranslation(dx, dy);
		beam.transform(at);
		
		if (retBool)
			target.decreaseHealth(beamDamage);
		return retBool;
	}
}
