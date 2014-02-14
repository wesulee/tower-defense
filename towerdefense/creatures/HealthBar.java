package towerdefense.creatures;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar
{
	private final Creature creature;
	private final int maxHealth;
	private Color currentColor;
	private int drawWidth;
	
	private static final int HEIGHT = 5;
	private static final int WIDTH = 40;
	private static final Color colors[] = {
		new Color(138, 23, 23),		// 0-9%
		new Color(173, 26, 26),		// 10-19%
		new Color(255, 23, 23),		// 20-29%
		new Color(255, 23, 23),		// 30-39%
		new Color(255, 23, 23),		// 40-49%
		new Color(255, 23, 23),		// 50-59%
		new Color(255, 23, 23),		// 60-69%
		new Color(2, 158, 37),		// 70-79%
		new Color(2, 212, 37),		// 80-89%
		new Color(2, 235, 37),		// 90-100%
	};
	private final int xOffset;
	private final int yOffset;
	
	public HealthBar(Creature c)
	{
		this.creature = c;
		this.maxHealth = c.getType().getHealth();
		xOffset = WIDTH / 2;
		yOffset = 30;
		update();
	}
	
	// creature's health has changed
	public void update()
	{
		int percent = minZero(creature.getHealth()) * 100 / maxHealth;
		drawWidth = (int)((float)percent * WIDTH / 100);
		if (creature.getHealth() != maxHealth)
			currentColor = colors[percent / 10];
		else
			currentColor = colors[9];
	}
	
	public void draw(Graphics2D g)
	{
		Color oldColor = g.getColor();
		g.setColor(Color.black);
		// black background, 1 px stroke
		g.fillRect((int)creature.getPositionX() - xOffset - 1,
				(int)creature.getPositionY() - yOffset - 1,
				WIDTH + 2, HEIGHT + 2);
		g.setColor(currentColor);
		g.fillRect((int)creature.getPositionX() - xOffset,
				(int)creature.getPositionY()- yOffset,
				drawWidth, HEIGHT);
		g.setColor(oldColor);
	}
	
	private int minZero(int a) {return a >= 0 ? a : 0;}
}
