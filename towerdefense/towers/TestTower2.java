package towerdefense.towers;

import java.awt.Color;
import java.util.List;

import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.Projectile;
import towerdefense.projectiles.RailBeam;

public class TestTower2 extends GameTower
{
	private static final TowerType tt = TowerType.TestTower2Type;
	// projectile info
	private final Color beamColor = Color.green;
	private final int beamWidth = 6;
	private final int beamDuration = 100;
	
	public TestTower2(RunningGame rg, int posX, int posY)
	{
		super(rg, tt, posX, posY);
	}
		
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
