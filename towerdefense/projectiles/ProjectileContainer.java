package towerdefense.projectiles;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ProjectileContainer
{
	private final ArrayList<Projectile> projectiles;
	
	public ProjectileContainer()
	{
		projectiles = new ArrayList<Projectile>();
	}
	
	public void update()
	{
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile proj = projectiles.get(i);
			if (proj.update()) {
				projectiles.remove(i);
				i--;
			}
		}
	}
	
	public void draw(Graphics2D g)
	{
		for (Projectile proj : projectiles)
			proj.draw(g);
	}
	
	public void add(Projectile proj) {projectiles.add(proj);}
}
