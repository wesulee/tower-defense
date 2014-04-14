package towerdefense.towers;

import java.util.List;

import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.ChainLightning;
import towerdefense.projectiles.Projectile;

public class ChainLightningTower extends GameTower
{
	private static final TowerType tt = TowerType.CLTowerType;
	
	public ChainLightningTower(RunningGame rg, int posX, int posY)
	{
		super(rg, tt, posX, posY);
	}

	public void attack(long time, List<Creature> targets)
	{
		if (!targets.isEmpty()) {
			Projectile proj = new ChainLightning(this, targets.get(0));
			tc.getProjectileContainer().add(proj);
			setLastAttack(time);
		}
	}
}
