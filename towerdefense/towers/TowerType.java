package towerdefense.towers;

/**
 * Contains defaults for towers
 */
public enum TowerType 
{
	TestTowerType("Test Tower", "test_tower.png", 10.0, 100, 2.0, 15, 1), 
	TestTower2Type("Test Tower 2", "test_tower2.png", 10.0, 100, 2.0, 15, 1);
	
	TowerType(String name, String spriteName, double damage, int range, 
			double speed, int size, int cost)
	{
		this.name = name;
		this.spriteName = spriteName;
		this.damage = damage;
		this.range = range;
		this.speed = speed;
		this.size = size;
		this.cost = cost;
	}
	
	private final String name;
	private final String spriteName;
	private final double damage;		// damage per attack
	private final int range;			// distance tower can hit target
	private final double speed;			// attacks per second
	private final int size;				// radius of tower
	private final int cost;				// purchase cost
	public String getName() {return name;}
	public String getSpriteName() {return spriteName;}
	public double getDamage() {return damage;}
	public int getRange() {return range;}
	public double getSpeed() {return speed;}
	public int getSize() {return size;}
	public int getCost() {return cost;}
}
