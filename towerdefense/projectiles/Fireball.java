package towerdefense.projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.animation.Fadeout;
import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureContainer;
import towerdefense.gamestates.RunningGame;
import towerdefense.towers.Tower;

public class Fireball extends ConstSpeedProjectile implements Projectile
{
	private static final int speed = 50;
	private static final int radius = 75;
	public static final String path1 = "Projectiles/fireball_small.png";
	public static final String path2 = "Projectiles/fireball_large.png";
	private final BufferedImage proj;
	private final BufferedImage aoe;
	private final int offsetX;
	private final int offsetY;
	private final int damage;
	private final CreatureContainer cc;
	private int phase = 0;
	private final Fadeout aoeFade;
	private final int destX;
	private final int destY;
	
	public Fireball(Tower src, int destX, int destY, RunningGame rg)
	{
		super(src.getX(), src.getY(), destX, destY, speed);
		
		this.damage = (int)src.getDamage();
		this.cc = rg.getCreatureContainer();
		this.destX = destX;
		this.destY = destY;
		
		this.proj = rg.getAssetLoader().get(path1);
		this.aoe = rg.getAssetLoader().get(path2);
		this.offsetX = proj.getWidth() / 2;
		this.offsetY = proj.getHeight() / 2;
		
		this.aoeFade = new Fadeout(aoe, destX, destY, 600);
	}
	
	public boolean update()
	{
		if (phase == 0) {
			if (super.update()) {
				phase = 1;
				// damage creatures in radius
				for (Creature c : cc.getCreaturesNear(destX, destY, radius))
					c.decreaseHealth(damage);
			}
			return false;
			
		}
		else
			return aoeFade.update();
	}
	
	public void draw(Graphics2D g)
	{
		if (phase == 0)
			g.drawImage(proj, (int)getX()-offsetX, (int)getY()-offsetY, null);
		else
			aoeFade.draw(g);
	}
}
