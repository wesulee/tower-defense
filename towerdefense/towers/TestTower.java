package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import towerdefense.SpriteContainer;
import towerdefense.creatures.Creature;

public class TestTower extends Tower
{
	private static final TowerType tt = TowerType.TestTowerType;
	private static BufferedImage sprite;
	// drawing offsets
	private static int spriteX = 0;
	private static int spriteY = 0;
	private Creature target = null;
	
	public TestTower(int pos_x, int pos_y)
	{
		super(tt.getDamage(), tt.getRange(), tt.getSpeed(), tt.getSize(),
				tt.getCost(), pos_x, pos_y);
		if (sprite == null) {
			sprite = SpriteContainer.getSprite(tt);
			spriteX = sprite.getWidth() / 2;
			spriteY = sprite.getHeight() / 2;
		}
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(sprite, getX() - spriteX, getY() - spriteY, null);
	}
	
	public TowerType getType() {return tt;}
	
	public void attack(ArrayList<Creature> eligibleTargets)
	{
		// not implemented
	}
}
