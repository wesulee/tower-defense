package towerdefense.towers;

public class TowerFactory
{	
	private TowerFactory() {}
	
	public static Tower newTower(TowerContainer tc, TowerType tt, int x, int y)
	{
		Tower nt;
		switch (tt) {
		case TestTowerType:
			nt = new TestTower(tc, x, y);
			break;
		case TestTower2Type:
			nt = new TestTower2(tc, x, y);
			break;
		default:
			nt = null;
		}
		
		return nt;
	}
}
