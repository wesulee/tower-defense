package towerdefense.towers;

public class TowerFactory
{
	private static TowerContainer tc;
	
	public TowerFactory(TowerContainer tc) {this.tc = tc;}
	
	public Tower newTower(TowerType tt, int x, int y)
	{
		Tower nt;
		switch (tt) {
		case TestTowerType:
			nt = new TestTower(tc, x, y);
			break;
		case TestTower2Type:
			nt = new TestTower2(x, y);
			break;
		default:
			nt = null;
		}
		
		return nt;
	}
}
