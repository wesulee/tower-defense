package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import towerdefense.AssetLoader;
import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.Fireball;
import towerdefense.projectiles.Projectile;

public class TestTower3 extends Tower
{
	private static final TowerType tt = TowerType.TestTower3Type;
	private static BufferedImage sprite;
	// drawing offsets
	private static int spriteX = 0;
	private static int spriteY = 0;
	private final TowerContainer tc;
	private final RunningGame rg;
	
	public TestTower3(RunningGame rg, int pos_x, int pos_y)
	{
		super(tt.getDamage(), tt.getRange(), tt.getSpeed(), tt.getSize(),
				tt.getCost(), pos_x, pos_y);
		if (sprite == null) {
			sprite = AssetLoader.getSprite(tt);
			spriteX = sprite.getWidth() / 2;
			spriteY = sprite.getHeight() / 2;
		}
		this.rg = rg;
		this.tc = rg.getTowerContainer();
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(sprite, getX() - spriteX, getY() - spriteY, null);
	}
	
	public void attack(long time, List<Creature> eligibleTargets)
	{
		if (!eligibleTargets.isEmpty()) {
			Creature c = eligibleTargets.get(0);
			Projectile proj = new Fireball(this, (int)c.getPositionX(),
					(int)c.getPositionY(), rg);
			tc.getProjectileContainer().add(proj);
			setLastAttack(time);
		}
	}
	
	public TowerType getType() {return tt;}
}
