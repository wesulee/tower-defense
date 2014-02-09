package towerdefense.creatures;

public enum CreatureType
{
	TestCreatureType("Test Creature", "creature_spider.png", 50, 10, 10, 10, 5);
	
	CreatureType(String name, String spriteName, int health, int speed,
			int sizeX, int sizeY, int goldDrop)
	{
		this.name = name;
		this.spriteName = spriteName;
		this.health = health;
		this.speed = speed;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.goldDrop = goldDrop;
	}
	
	private final String name;
	private final String spriteName;
	private final int health;
	private final int speed;
	// pixel size for creature sprite going north
	private final int sizeX;
	private final int sizeY;
	private final int goldDrop;
	
	public String getName() {return name;}
	public String getSpriteName() {return spriteName;}
	public int getHealth() {return health;}
	public int getSpeed() {return speed;}
	public int getSizeX() {return sizeX;}
	public int getSizeY() {return sizeY;}
	public int getGoldDrop() {return goldDrop;}
}