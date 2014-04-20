package towerdefense.creatures;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.TowerDefense;
import towerdefense.util.Utility;

/**
 * Extends Creature to supporting drawing.
 */
public class GameCreature extends Creature
{
	private BufferedImage effect = null;
	private int effectOffsetX;	// drawing offset of effect
	private int effectOffsetY;
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
		
		if (effect != null)
			g.drawImage(effect, (int) posX - effectOffsetX,
					(int) posY - effectOffsetY, null);
		
		healthBar.draw(g);
		
		if (TowerDefense.DEBUG) {
			g.draw(getRectangle());
			Utility.drawCrosshair(g, (int) posX, (int) posY, 2);
		}
	}
	
	// draw target rectangle
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
	
	public void setDrawEffect(BufferedImage img)
	{
		effect = img;
		effectOffsetX = img.getWidth() / 2;
		effectOffsetY = img.getHeight() / 2;
	}
	
	public  void clearDrawEffect() {effect = null;}
}
