package towerdefense.towers;

public enum TowerType 
{
	TestTowerType("test_tower.png"), TestTower2Type("test_tower2.png");
	
	TowerType(String fname)
	{
		name = fname;
	}
	
	private final String name;
	public String getFileName() {return name;}
}
