package towerdefense.towers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import towerdefense.SpriteContainer;
import towerdefense.creatures.Creature;
import towerdefense.projectiles.Projectile;
import towerdefense.projectiles.RailBeam;

public class TestTower2 extends Tower
{
	private static final TowerType tt = TowerType.TestTower2Type;
	private static BufferedImage sprite;
	private static int spriteX = 0;
	private static int spriteY = 0;
	private static TowerContainer tc;
	// projectile info
	private final Color beamColor = Color.green;
	private final int beamWidth = 6;
	private final int beamDuration = 100;
	
	public TestTower2(TowerContainer tc, int pos_x, int pos_y)
	{
		super(tt.getDamage(), tt.getRange(), tt.getSpeed(), tt.getSize(),
				tt.getCost(), pos_x, pos_y);
		if (sprite == null) {
			sprite = SpriteContainer.getSprite(tt);
			spriteX = sprite.getWidth() / 2;
			spriteY = sprite.getHeight() / 2;
		}
		this.tc = tc;
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(sprite, getX() - spriteX, getY() - spriteY, null);
	}
	
	public TowerType getType() {return tt;}
	
	public void attack(long time, List<Creature> eligibleTargets)
	{
		if (!eligibleTargets.isEmpty()) {
			Creature c = eligibleTargets.get(0);
			Projectile proj = new RailBeam(tc, this, c, beamDuration,
					beamWidth, beamColor);
			tc.getProjectileContainer().add(proj);
			setLastAttack(time);
		}
	}
}
