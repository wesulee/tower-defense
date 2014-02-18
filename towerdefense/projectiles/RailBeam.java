package towerdefense.projectiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import towerdefense.GamePanel;
import towerdefense.Utility;
import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureContainer;
import towerdefense.towers.Tower;
import towerdefense.towers.TowerContainer;

/**
 * A long beam that immediately damages all creatures in its path.
 */
public class RailBeam implements Projectile
{
	private static int beamLength = (int)Utility.length(GamePanel.MENU_X,
			GamePanel.HEIGHT);
	private final int maxUpdateTicks;
	private int updateTickCounter = 0;
	private final int beamHitAllowance = 10;
	
	private final int x1;
	private final int y1;
	private final int x2;
	private final int y2;
	private final Color beamColor;
	private final Stroke beamStroke;
	private final Path2D.Double beam;
	private final CreatureContainer cc;
	
	// beam guaranteed to pass through and damage Creature c
	public RailBeam(TowerContainer tc, Tower t, Creature c, int milliseconds,
			int beamWidth, Color beamColor)
	{
		long milliToNano = milliseconds * 1000000L;
		this.maxUpdateTicks = (int)(milliToNano / GamePanel.period) + 1;
		this.cc = tc.getGamePanel().getCreatureContainer();
		this.beamColor = beamColor;
		this.beamStroke = new BasicStroke(beamWidth);
		
		double dirX = c.getPositionX() - t.getX();
		double dirY = c.getPositionY() - t.getY();
		double dirLen = Math.sqrt(dirX*dirX + dirY*dirY);
		x1 = (int)(t.getX() + dirX * t.getSize() / dirLen);
		y1 = (int)(t.getY() + dirY * t.getSize() / dirLen);
		x2 = (int)(x1 + dirX * beamLength / dirLen);
		y2 = (int)(y1 + dirY * beamLength / dirLen);
		
		double angle = Math.atan2(dirY, dirX);
		Rectangle beamRect = new Rectangle(
				x1,
				y1 - (beamWidth+beamHitAllowance) / 2,
				GamePanel.MENU_X,
				beamWidth + beamHitAllowance
		);
		beam = new Path2D.Double();
		beam.append(beamRect, false);
		AffineTransform at = new AffineTransform();
		at.rotate(angle, beamRect.x, beamRect.y + beamRect.height / 2);
		beam.transform(at);
		
		// damage creatures, incomplete
		for (Creature test : cc.getCreatureList()) {
			if (creatureInBeam(test))
				test.decreaseHealth((int)t.getDamage());
		}
	}
	
	public void draw(Graphics2D g)
	{
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		
		g.setColor(beamColor);
		g.setStroke(beamStroke);
		g.drawLine(x1, y1, x2, y2);
		
		g.setColor(oldColor);
		g.setStroke(oldStroke);
	}

	public boolean update() {
		if (++updateTickCounter > maxUpdateTicks)
			return true;
		return false;
	}
	
	// beam is a rotated 
	private boolean creatureInBeam(Creature c)
	{
		return beam.contains(c.getPositionX(), c.getPositionY());
	}

}
