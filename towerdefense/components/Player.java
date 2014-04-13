package towerdefense.components;

import towerdefense.gamestates.RunningGame;

public final class Player
{
	private int health;
	private final int maxHealth;
	private int gold;
	
	private final static int DEFAULT_HEALTH = 20;
	private final static int DEFAULT_GOLD = 50;
	
	private final RunningGame rg;
	
	public Player(RunningGame rg)
	{
		this.health = DEFAULT_HEALTH;
		this.maxHealth = DEFAULT_HEALTH;
		this.gold = DEFAULT_GOLD;
		this.rg = rg;
	}
	
	// return remaining health
	public int decreaseHealth(int amount)
	{
		health -= amount;
		if (health <= 0)
			rg.notifyGameLost();
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
		rg.getMenu().notifyGoldChange();
		return gold;
	}
	
	public void increaseGold(int amount)
	{
		gold += amount;
		rg.getMenu().notifyGoldChange();
	}
	
	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public int getGold() {return gold;}
}
