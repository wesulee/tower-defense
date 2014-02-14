package towerdefense.projectiles;

import java.awt.Graphics2D;

public interface Projectile {
	public void draw(Graphics2D g);
	public boolean update();
}
