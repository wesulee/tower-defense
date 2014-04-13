package towerdefense.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import towerdefense.GamePanel;

public class CenteredText
{
	private final String str;
	private final int drawStringX;
	private final int drawStringY;
	private final Color color;
	
	public CenteredText(GamePanel gp, String str, int offsetY, Color color)
	{
		this.str = str;
		Rectangle2D rect = gp.getStringBounds(str);
		this.drawStringX = (GamePanel.WIDTH - (int)rect.getWidth()) / 2;
		this.drawStringY = (GamePanel.HEIGHT - (int)rect.getHeight()) / 2
				+ offsetY;
		this.color = color;
	}

	public void draw(Graphics2D g)
	{
		draw(g, str);
	}
	
	public void draw(final Graphics2D g, String string)
	{
		final Color oldColor = g.getColor();
		
		g.setColor(color);
		g.drawString(string, drawStringX, drawStringY);
		
		g.setColor(oldColor);
	}
}
