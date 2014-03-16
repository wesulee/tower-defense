package towerdefense.animation;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.GamePanel;

/**
 * Drawns an image centered at (x, y) fading out.
 */
public class Fadeout implements Animation
{
	private final BufferedImage img;
	private float alpha = 1.0f;
	private final float da;
	private int updateTickCounter = 0;
	private final int maxUpdateTicks;
	private final int x;
	private final int y;
	
	// ms: milliseconds fading duration
	public Fadeout(BufferedImage img, int x, int y, int ms)
	{
		this.img = img;
		this.x = x - img.getWidth() / 2;
		this.y = y - img.getHeight() / 2;
		
		long milliToNano = ms * 1000000L;
		this.maxUpdateTicks = (int)(milliToNano / GamePanel.period) + 1;
		this.da = -1.0f / maxUpdateTicks;
	}

	public void draw(Graphics2D g)
	{
		Composite oldComposite = g.getComposite();
		
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha));
		g.drawImage(img, x, y, null);
		
		g.setComposite(oldComposite);
	}

	public boolean update()
	{
		if (++updateTickCounter == maxUpdateTicks)
			return true;
		else {
			alpha += da;
			return false;
		}
	}
}
