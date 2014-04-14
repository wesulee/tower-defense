package towerdefense.creatures.effects;

import towerdefense.creatures.Creature;

public interface CreatureEffectSource
{
	public CreatureEffect applyEffect(Creature c);
	public CreatureEffectType getType();
}
