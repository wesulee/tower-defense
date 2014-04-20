package towerdefense.projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import towerdefense.animation.Fadeout;
import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureContainer;
import towerdefense.creatures.effects.CreatureEffect;
import towerdefense.creatures.effects.CreatureEffectSource;
import towerdefense.creatures.effects.CreatureEffectType;
import towerdefense.creatures.effects.SpeedModifier;
import towerdefense.gamestates.RunningGame;
import towerdefense.towers.FrostTower;

public class Frost implements Projectile, CreatureEffectSource
{
	public static final String path1 = "Projectiles/frost.png";
	public static final String path2 = "Projectiles/frost_creature.png";
	private static final int duration = 1000;	// ms
	private static final double speedModifier = 0.8;
	private static final int effectDuration = 5000;
	private final RunningGame rg;
	private final Fadeout fade;
	
	public Frost(FrostTower src, List<Creature> creatures, RunningGame rg)
	{
		this.rg = rg;
		BufferedImage sprite = rg.getAssetLoader().get(path1);
		this.fade = new Fadeout(sprite, src.getX(), src.getY(), duration);
		
		final CreatureContainer cc = rg.getCreatureContainer();
		for (Creature c : creatures) {
			c.decreaseHealth((int)src.getDamage());
			cc.applyCreatureEffect(c, this);
		}
	} 

	public void draw(final Graphics2D g)
	{
		fade.draw(g);
	}

	public boolean update()
	{
		return fade.update();
	}

	public CreatureEffect applyEffect(Creature c)
	{
		return new SpeedModifier(c, speedModifier, effectDuration,
				rg.getAssetLoader().get(path2));
	}

	public CreatureEffectType getType()
	{
		return CreatureEffectType.SpeedModifier;
	}
}
