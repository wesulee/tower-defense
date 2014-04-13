package towerdefense.animation;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;

import towerdefense.GamePanel;

public class ShapeFadeIn implements Animation
{
	private final Shape shape;
	private int updateTickCounter = 0;
	private final int maxUpdateTicks;
	private float alpha = 0f;
	private final float da;
	
	public ShapeFadeIn(Shape s, int ms, float finalAlpha)
	{
		this.shape = s;
		long milliToNano = ms * 1000000L;
		this.maxUpdateTicks = (int)(milliToNano / GamePanel.period) + 1;
		this.da = finalAlpha / maxUpdateTicks;
	}

	public void draw(final Graphics2D g)
	{
		Composite oldComposite = g.getComposite();
		
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha));
		g.fill(shape);
		
		g.setComposite(oldComposite);
	}

	public boolean update()
	{
		if (++updateTickCounter >= maxUpdateTicks)
			return true;
		else {
			alpha += da;
			return false;
		}
	}
}
