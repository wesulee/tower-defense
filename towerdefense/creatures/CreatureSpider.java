package towerdefense.creatures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.Direction;
import towerdefense.SpriteContainer;

public class CreatureSpider extends Creature
{
	private static final CreatureType ct = CreatureType.Spider;
	private static final BufferedImage sprites[] = 
			SpriteContainer.loadCreature(ct);
	private Direction spriteDir = Direction.N;	//default value
	private static final int spriteOffsetsX[] =
			getSpriteOffsetsX(sprites);
	private static final int spriteOffsetsY[] =
			getSpriteOffsetsY(sprites);
	
	public CreatureSpider(double pos_x, double pos_y)
	{
		super(ct.getHealth(), ct.getSpeed(), ct.getGoldDrop(), pos_x, pos_y);
		super.rect.width = ct.getSizeX();
		super.rect.height = ct.getSizeY();
		setPosition(pos_x, pos_y);
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(sprites[spriteDir.toIndex()],
				(int)getPositionX() - spriteOffsetsX[spriteDir.toIndex()],
				(int)getPositionY() - spriteOffsetsY[spriteDir.toIndex()],
				null);
		g.draw(super.rect);
		drawHealthBar(g);
	}
	
	public void setPosition(double x, double y)
	{
		super.pos_x = x;
		super.pos_y = y;
		super.rect.x = (int)x - ct.getSizeX() / 2;
		super.rect.y = (int)y - ct.getSizeY() / 2;
	}
	
	public CreatureType getType() {return ct;}
	
	private static int[] getSpriteOffsetsX(BufferedImage sprites[])
	{
		int offsets[] = new int[sprites.length];
		for (int i = 0; i < sprites.length; i++)
			offsets[i] = sprites[i].getWidth() / 2;
		return offsets;
	}
	
	private static int[] getSpriteOffsetsY(BufferedImage sprites[])
	{
		int offsets[] = new int[sprites.length];
		for (int i = 0; i < sprites.length; i++)
			offsets[i] = sprites[i].getHeight() / 2;
		return offsets;
	}
}
