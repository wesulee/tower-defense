package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import towerdefense.SpriteContainer;
import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.Projectile;
import towerdefense.projectiles.TestTowerProjectile;

public class TestTower extends Tower
{
	private static final TowerType tt = TowerType.TestTowerType;
	private static BufferedImage sprite;
	// drawing offsets
	private static int spriteX = 0;
	private static int spriteY = 0;
	private static TowerContainer tc;
	
	public TestTower(RunningGame rg, int pos_x, int pos_y)
	{
		super(tt.getDamage(), tt.getRange(), tt.getSpeed(), tt.getSize(),
				tt.getCost(), pos_x, pos_y);
		if (sprite == null) {
			sprite = SpriteContainer.getSprite(tt);
			spriteX = sprite.getWidth() / 2;
			spriteY = sprite.getHeight() / 2;
		}
		this.tc = rg.getTowerContainer();
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(sprite, getX() - spriteX, getY() - spriteY, null);
	}
	
	public TowerType getType() {return tt;}
	
	// assumes tower is not on attack cooldown and
	// eligible targets are all in range
	public void attack(long time, List<Creature> eligibleTargets)
	{
		if (!eligibleTargets.isEmpty()) {
			Creature c = eligibleTargets.get(0);
			Projectile proj = new TestTowerProjectile(getX(), getY(), c);
			tc.getProjectileContainer().add(proj);
			setLastAttack(time);
		}
	}
}
