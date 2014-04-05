package towerdefense.towers;

import java.util.List;

import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.Frost;
import towerdefense.projectiles.Projectile;

public class FrostTower extends GameTower
{
	private static final TowerType tt = TowerType.FrostTowerType;
	
	public FrostTower(RunningGame rg, int posX, int posY)
	{
		super(rg, tt, posX, posY);
	}

	public void attack(final long time, final List<Creature> creatures)
	{
		if (!creatures.isEmpty()) {
			Projectile proj = new Frost(this, creatures, rg);
			tc.getProjectileContainer().add(proj);
			setLastAttack(time);
		}
	}
}
