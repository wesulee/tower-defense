package towerdefense.animation;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.util.Counter;

/**
 * Drawns an image centered at (x, y) fading out.
 */
public class Fadeout implements Animation
{
	private final BufferedImage img;
	private float alpha = 1.0f;
	private final float da;
	private final Counter counter;
	private final int x;
	private final int y;
	
	// ms: milliseconds fading duration
	public Fadeout(BufferedImage img, int x, int y, int ms)
	{
		this.img = img;
		this.x = x - img.getWidth() / 2;
		this.y = y - img.getHeight() / 2;
		
		this.counter = new Counter(ms);
		// 0.999 rather than 1 to ensure alpha is never less than 0
		this.da = -0.999f / counter.getMaxTicks();
	}

	public void draw(final Graphics2D g)
	{
		final Composite oldComposite = g.getComposite();
		
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha));
		g.drawImage(img, x, y, null);
		
		g.setComposite(oldComposite);
	}

	public boolean update()
	{
		if (counter.update()) {
			return true;
		}
		else {
			alpha += da;
			return false;
		}
	}
}
