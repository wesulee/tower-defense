package towerdefense.creatures.effects;

import towerdefense.creatures.Creature;
import towerdefense.util.Counter;

public class SpeedModifier implements CreatureEffect
{
	private static final CreatureEffectType type =
			CreatureEffectType.SpeedModifier;
	private final Creature c;
	private final Counter counter;
	
	public SpeedModifier(Creature c, double modifier, int ms)
	{
		this.c = c;
		this.counter = new Counter(ms);
		c.setSpeedMult(modifier);
	}
	
	public boolean update()
	{
		if (counter.update()) {	// undo effect
			c.setSpeedMult(1.0);	// assumption
			return true;
		}
		return false;
	}
	
	// reset the duration of the effect
	public void reapplyEffect()
	{
		counter.reset();
	}
	
	public CreatureEffectType getType() {return type;}
}
