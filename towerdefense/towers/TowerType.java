package towerdefense.towers;

/**
 * Contains defaults for towers
 */
public enum TowerType 
{
	TestTowerType("Test Tower", "test_tower.png", "Test Tower description here",
			10.0, 200, 2.0, 25, 1, 50, 50), 
	TestTower2Type("Test Tower 2", "test_tower2.png", "None",
			10.0, 100, 2.0, 25, 10, 50, 50),
	TestTower3Type("Test Tower 3", "test_tower2.png", "None",
			10.0, 100, 2.0, 25, 20, 50, 50);
	
	TowerType(String name, String spriteName, String description, 
			double damage, int range, double speed, int size, int cost,
			int spriteWidth, int spriteHeight)
	{
		this.name = name;
		this.spriteName = spriteName;
		this.description = description;
		this.damage = damage;
		this.range = range;
		this.speed = speed;
		this.size = size;
		this.cost = cost;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
	}
	
	private final String name;
	private final String spriteName;
	private final String description;
	private final double damage;		// damage per attack
	private final int range;			// distance tower can hit target
	private final double speed;			// attacks per second
	private final int size;				// radius of tower
	private final int cost;				// purchase cost
	private final int spriteWidth;
	private final int spriteHeight;
	public String getName() {return name;}
	public String getSpriteName() {return spriteName;}
	public String getDescription() {return description;}
	public double getDamage() {return damage;}
	public int getRange() {return range;}
	public double getSpeed() {return speed;}
	public int getSize() {return size;}
	public int getCost() {return cost;}
	public int getSpriteWidth() {return spriteWidth;}
	public int getSpriteHeight() {return spriteHeight;}
}
