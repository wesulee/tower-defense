package towerdefense.towers;

import java.util.List;

import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.Fireball;
import towerdefense.projectiles.Projectile;

public class TestTower3 extends GameTower
{
	private static final TowerType tt = TowerType.TestTower3Type;
	
	public TestTower3(RunningGame rg, int posX, int posY)
	{
		super(rg, tt, posX, posY);
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
}
