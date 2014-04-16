package towerdefense.creatures.effects;

import towerdefense.creatures.Creature;
import towerdefense.util.Counter;
import towerdefense.util.Utility;

public class DamageOverTime implements CreatureEffect
{
	private static final CreatureEffectType type =
			CreatureEffectType.DamageOverTime;
	private final Creature c;
	private final Counter counter;
	private final int tickMod;
	private final int damage;
	
	public DamageOverTime(Creature c, int damage, int totalDuration,
			int tickDuration)
	{
		this.c = c;
		this.counter = new Counter(totalDuration);
		this.tickMod = Utility.msToGameTicks(tickDuration);
		this.damage = damage;
	}

	public boolean update()
	{
		final boolean ret = counter.update();
		if (counter.getTicks() % tickMod == 0)
			c.decreaseHealth(damage);
		return ret;
	}

	public void reapplyEffect() {counter.reset();}
	public CreatureEffectType getType() {return type;}
}
