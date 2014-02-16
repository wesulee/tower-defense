package towerdefense.creatures;

/**
 * Speed is distance (in pixels) traveled in 1 sec
 */
public enum CreatureType
{
	Spider("Spider", "spider", ".png", 50, 60, 50, 50, 5);
	
	CreatureType(String name, String spriteName, String extension,
			int health, int speed, int sizeX, int sizeY, int goldDrop)
	{
		this.name = name;
		this.spriteName = spriteName;
		this.spriteExtension = extension;
		this.health = health;
		this.speed = speed;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.goldDrop = goldDrop;
	}
	
	private final String name;
	private final String spriteName;
	private final String spriteExtension;
	private final int health;
	private final int speed;
	// pixel size for creature sprite going north
	private final int sizeX;
	private final int sizeY;
	private final int goldDrop;
	
	public String getName() {return name;}
	public String getSpriteName() {return spriteName;}
	public String getSpriteExtension() {return spriteExtension;}
	public int getHealth() {return health;}
	public int getSpeed() {return speed;}
	public int getSizeX() {return sizeX;}
	public int getSizeY() {return sizeY;}
	public int getGoldDrop() {return goldDrop;}
}