package towerdefense;

/**
 * Menu must be set in order to function correctly.
 * Correct order: p = new Player(); m = new Menu(...); p.setMenu(m);
 */
public class Player
{
	private int health;
	private final int maxHealth;
	private int gold;
	
	private final static int DEFAULT_HEALTH = 20;
	private final static int DEFAULT_GOLD = 50;
	
	private Menu menu;
	
	public Player()
	{
		this.health = DEFAULT_HEALTH;
		this.maxHealth = DEFAULT_HEALTH;
		this.gold = DEFAULT_GOLD;
	}
	
	// return remaining health
	public int decreaseHealth(int amount)
	{
		health -= amount;
		return health;
	}
	
	public boolean isAlive()
	{
		return health > 0;
	}
	
	// returns remaining gold
	public int decreaseGold(int amount)
	{
		gold -= amount;
		if (menu != null) menu.notifyGoldChange();
		return gold;
	}
	
	public void increaseGold(int amount) {gold += amount;}
	
	public void setMenu(Menu menu) {this.menu = menu;}
	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public int getGold() {return gold;}
}
