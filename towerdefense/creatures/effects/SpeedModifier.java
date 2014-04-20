package towerdefense.creatures.effects;

import java.awt.image.BufferedImage;

import towerdefense.creatures.Creature;
import towerdefense.creatures.GameCreature;
import towerdefense.util.Counter;

public class SpeedModifier implements CreatureEffect
{
	private static final CreatureEffectType type =
			CreatureEffectType.SpeedModifier;
	private final Creature c;
	private final Counter counter;
	private final BufferedImage img;
	
	public SpeedModifier(Creature c, double modifier, int ms)
	{
		this.c = c;
		this.counter = new Counter(ms);
		c.setSpeedMult(modifier);
		img = null;
	}
	
	// while creature is affected by this effect, have img drawn over creature
	public SpeedModifier(Creature c, double modifier, int ms,
			BufferedImage img)
	{
		this.c = c;
		this.counter = new Counter(ms);
		this.img = img;
		c.setSpeedMult(modifier);
		((GameCreature) c).setDrawEffect(img);
		
	}
	
	public boolean update()
	{
		if (counter.update()) {	// undo effect
			c.setSpeedMult(1.0);	// assumption
			if (img != null)
				((GameCreature) c).clearDrawEffect();
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
