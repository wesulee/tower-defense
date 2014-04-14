package towerdefense.creatures.effects;

public interface CreatureEffect
{
	public boolean update();
	public void reapplyEffect();
	public CreatureEffectType getType();
}
