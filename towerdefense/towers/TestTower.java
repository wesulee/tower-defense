package towerdefense.towers;

import java.util.List;

import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.Projectile;
import towerdefense.projectiles.TestTowerProjectile;

public class TestTower extends GameTower
{
	private static final TowerType tt = TowerType.TestTowerType;
	
	public TestTower(RunningGame rg, int posX, int posY)
	{
		super(rg, tt, posX, posY);
	}
	
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
