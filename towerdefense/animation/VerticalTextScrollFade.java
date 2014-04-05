package towerdefense.animation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;

import towerdefense.GamePanel;

/**
 * Displays a vertically scrolling string that fades out after a given amount
 * of milliseconds. Opacity and position are updated linearly.
 */
public class VerticalTextScrollFade implements Animation
{
	private final String string;
	private final Font font;
	private final Color color;
	private final int x;
	private float y;
	private final float dy;
	private float alpha = 1.0f;
	private final float da;
	private int updateTickCounter = 0;
	private final int maxUpdateTicks;
	
	public VerticalTextScrollFade(String str, int x1, int y1, int y2, int ms,
			Font font, Color color)
	{
		this.string = str;
		this.font = font;
		this.color = color;
		long milliToNano = ms * 1000000L;
		this.maxUpdateTicks = (int)(milliToNano / GamePanel.period) + 1;
		this.x = x1;
		this.y = y1;
		this.dy = (float)(y2 - y1) / maxUpdateTicks;
		this.da = -1.0f / (maxUpdateTicks + 1);
		
	}
	
	public void draw(Graphics2D g)
	{
		final Font oldFont = g.getFont();
		final Color oldColor = g.getColor();
		final Composite oldComposite = g.getComposite();
		
		g.setFont(font);
		g.setColor(color);
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha));
		g.drawString(string, x, (int)y);
		
		g.setFont(oldFont);
		g.setColor(oldColor);
		g.setComposite(oldComposite);
	}

	public boolean update()
	{
		if (++updateTickCounter == maxUpdateTicks)
			return true;
		
		y += dy;
		alpha += da;
		
		return false;
	}
}
