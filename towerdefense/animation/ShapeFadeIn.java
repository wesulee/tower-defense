package towerdefense.animation;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;

import towerdefense.util.Counter;

public class ShapeFadeIn implements Animation
{
	private final Shape shape;
	private final Counter counter;
	private float alpha = 0f;
	private final float da;
	
	public ShapeFadeIn(Shape s, int ms, float finalAlpha)
	{
		this.shape = s;
		this.counter = new Counter(ms);
		this.da = finalAlpha / counter.getMaxTicks();
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
		if (counter.update())
			return true;
		else {
			alpha += da;
			return false;
		}
	}
}
