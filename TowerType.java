public enum TowerType 
{
	TestTowerType("test_tower.png");
	
	TowerType(String fname)
	{
		name = fname;
	}
	
	private final String name;
	public String getFileName() {return name;}
}
