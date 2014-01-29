public class Player
{
	private int health;
	private final int maxHealth;
	private int gold;
	
	private final static int DEFAULT_HEALTH = 20;
	private final static int DEFAULT_GOLD = 50;
	
	public Player()
	{
		this.health = DEFAULT_HEALTH;
		this.maxHealth = DEFAULT_HEALTH;
		this.gold = DEFAULT_GOLD;
	}
	
	// return false when health <= health, else true
	public boolean decreaseHealth(int amount)
	{
		health -= amount;
		return health <= 0;
	}
	
	public boolean isAlive()
	{
		return health > 0;
	}
	
	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public int getGold() {return gold;}
}
