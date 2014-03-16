package towerdefense.projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.SpriteContainer;
import towerdefense.animation.Fadeout;
import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureContainer;
import towerdefense.towers.Tower;

public class Fireball extends ConstSpeedProjectile implements Projectile
{
	private static final int speed = 50;
	private static final int radius = 75;
	private static final BufferedImage proj = 
			SpriteContainer.loadProjectile("fireball_small.png");
	private static final BufferedImage aoe =
			SpriteContainer.loadProjectile("fireball_large.png");
	private static final int offsetX = proj.getWidth() / 2;
	private static final int offsetY = proj.getHeight() / 2;
	private final int damage;
	private final CreatureContainer cc;
	private int phase = 0;
	private Fadeout aoeFade;
	private final int destX;
	private final int destY;
	
	public Fireball(Tower src, int destX, int destY, CreatureContainer cc)
	{
		super(src.getX(), src.getY(), destX, destY, speed);
		
		this.damage = (int)src.getDamage();
		this.cc = cc;
		this.destX = destX;
		this.destY = destY;
		this.aoeFade = new Fadeout(aoe, destX, destY, 1000);
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
