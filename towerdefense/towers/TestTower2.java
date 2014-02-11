package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.SpriteContainer;

public class TestTower2 extends Tower
{
	private static final TowerType tt = TowerType.TestTower2Type;
	private static BufferedImage sprite;
	private static int spriteX = 0;
	private static int spriteY = 0;
	
	public TestTower2(int pos_x, int pos_y)
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
}
