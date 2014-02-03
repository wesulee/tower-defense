package towerdefense.towers;

import java.awt.Graphics2D;

public interface BasicTower 
{
	boolean canAttack(long time);
	void setLastAttack(long time);
	int getSize();
	int getCost();
	double getDamage();
	void draw(Graphics2D g);
	TowerType getTowerType();
	String getName();
}
