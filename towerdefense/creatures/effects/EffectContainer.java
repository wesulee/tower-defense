package towerdefense.creatures.effects;

import towerdefense.creatures.Creature;

/**
 * Manage the effects on a single Creature.
 */
public class EffectContainer
{
	private final Creature c;
	private final CreatureEffect[] effects;
	private int activeEffectsCount = 0;
	
	public EffectContainer(Creature c)
	{
		this.c = c;
		this.effects = new CreatureEffect[CreatureEffectType.values().length];
		this.activeEffectsCount = 0;
	}
	
	public EffectContainer(Creature c, CreatureEffectSource source)
	{
		this.c = c;
		this.effects = new CreatureEffect[CreatureEffectType.values().length];
		CreatureEffect ce = source.applyEffect(c);
		effects[ce.getType().ordinal()] = ce;
		this.activeEffectsCount = 1;
	}
	
	public void addEffect(CreatureEffectSource source)
	{
		final int i = source.getType().ordinal();
		if (effects[i] == null) {
			effects[i] = source.applyEffect(c);
			++activeEffectsCount;
		}
		else
			effects[i].reapplyEffect();
	}

	// returns true when no effects affecting c
	public boolean update()
	{
		for (CreatureEffect ef : effects) {
			if (ef != null && ef.update()) {
				effects[ef.getType().ordinal()] = null;
				--activeEffectsCount;
			}
		}
		
		return activeEffectsCount == 0;
	}
}
