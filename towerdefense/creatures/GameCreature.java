package towerdefense.creatures;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.TowerDefense;

/**
 * Extends Creature to supporting drawing.
 */
public class GameCreature extends Creature
{
	private final CreatureType ct;
	private final BufferedImage[] sprites;
	private final HealthBar healthBar;
	
	public GameCreature(CreatureType ct, double posX, double posY,
			BufferedImage[] sprites)
	{
		super(ct.getHealth(), ct.getSpeed(), ct.getGoldDrop(), posX, posY,
				ct.getSizeX(), ct.getSizeY());
		this.ct = ct;
		this.sprites = sprites;
		this.healthBar = new HealthBar(this);
	}
	
	public void draw(final Graphics2D g)
	{
		switch(dir) {
		case N:
		case S:
			g.drawImage(sprites[dir.toIndex()],
					(int)posX - sizeX / 2,
					(int)posY - sizeY / 2,
					null);
			break;
		case E:
		case W:
			g.drawImage(sprites[dir.toIndex()],
					(int)posX - sizeY / 2,
					(int)posY - sizeX / 2,
					null);
		}
		
		healthBar.draw(g);
		
		if (TowerDefense.DEBUG)
			g.draw(getRectangle());
	}
	
	public void drawDebug(final Graphics2D g)
	{
		final Composite oldComposite = g.getComposite();
		final Color oldColor = g.getColor();
		
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.5f));
		g.setColor(Color.green);
		g.fill(pathRect);
		
		g.setColor(oldColor);
		g.setComposite(oldComposite);
	}
	
	public void decreaseHealth(int amount)
	{
		super.decreaseHealth(amount);
		healthBar.update();
	}
	
	public CreatureType getType() {return ct;}
}
