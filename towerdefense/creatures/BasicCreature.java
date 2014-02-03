package towerdefense.creatures;

import java.awt.Graphics2D;

public interface BasicCreature 
{
	void draw(Graphics2D g);
	void setPosition(double x, double y);
	double getPositionX();
	double getPositionY();
}
