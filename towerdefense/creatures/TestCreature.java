package towerdefense.creatures;

import java.awt.Graphics2D;

import towerdefense.Direction;

public class TestCreature extends Creature
{
	private static final CreatureType ct = CreatureType.TestCreatureType;
	private Direction spriteDir;
	
	public TestCreature(double pos_x, double pos_y)
	{
		super(ct.getHealth(), ct.getSpeed(), ct.getGoldDrop(), pos_x, pos_y);
		super.rect.width = ct.getSizeX();
		super.rect.height = ct.getSizeY();
		setPosition(pos_x, pos_y);
	}

	public void draw(Graphics2D g)
	{
		
	}
	
	public void setPosition(double x, double y)
	{
		super.pos_x = x;
		super.pos_y = y;
		super.rect.x = (int) (x - ct.getSizeX() / 2);
		super.rect.y = (int) (y - ct.getSizeY() / 2);
	}
	
	public CreatureType getType() {return ct;}
}
