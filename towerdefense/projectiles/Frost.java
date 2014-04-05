package towerdefense.projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import towerdefense.animation.Fadeout;
import towerdefense.creatures.Creature;
import towerdefense.gamestates.RunningGame;
import towerdefense.towers.FrostTower;

public class Frost implements Projectile
{
	public static final String path = "Projectiles/frost.png";
	private static final int duration = 1000;	// ms
	private final Fadeout fade;
	
	public Frost(FrostTower src, List<Creature> creatures, RunningGame rg)
	{
		BufferedImage sprite = rg.getAssetLoader().get(path);
		fade = new Fadeout(sprite, src.getX(), src.getY(), duration);
		
		for (Creature c : creatures) {
			c.decreaseHealth((int)src.getDamage());
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
}
