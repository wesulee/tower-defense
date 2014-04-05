package towerdefense.towers;

import towerdefense.gamestates.RunningGame;

public final class TowerFactory
{	
	private TowerFactory() {}
	
	public static Tower newTower(RunningGame rg, TowerType tt, int x, int y)
	{
		Tower nt;
		switch (tt) {
		case TestTowerType:
			nt = new TestTower(rg, x, y);
			break;
		case TestTower2Type:
			nt = new TestTower2(rg, x, y);
			break;
		case TestTower3Type:
			nt = new TestTower3(rg, x, y);
			break;
		case FrostTowerType:
			nt = new FrostTower(rg, x, y);
			break;
		default:
			nt = null;
		}
		
		return nt;
	}
}
