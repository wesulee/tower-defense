package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TestCreature extends Creature implements BasicCreature
{
	private static final String name = "Test Creature";
	private static final int HEALTH = 50;
	private static final int SPEED = 10;
	private static final int GOLD_DROP = 10;
	private static BufferedImage sprite;
	
	
	public TestCreature(double pos_x, double pos_y)
	{
		super(HEALTH, SPEED, GOLD_DROP, pos_x, pos_y);
		
	}

	public void draw(Graphics2D g)
	{
		
	}
}
