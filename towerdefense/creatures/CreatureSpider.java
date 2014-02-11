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
	
	public CreatureSpider(double pos_x, double pos_y)
	{
		super(ct.getHealth(), ct.getSpeed(), ct.getGoldDrop(), pos_x, pos_y);
		super.rect.width = ct.getSizeX();
		super.rect.height = ct.getSizeY();
		setPosition(pos_x, pos_y);
	}

	public void draw(Graphics2D g)
	{
		// incomplete
		g.drawImage(sprites[spriteDir.toIndex()],
				(int)getPositionX(), (int)getPositionY(), null);
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
