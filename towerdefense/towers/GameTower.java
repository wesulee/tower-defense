package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import towerdefense.gamestates.RunningGame;

public abstract class GameTower extends Tower
{
	private final TowerType tt;
	private final BufferedImage sprite;
	protected final RunningGame rg;
	protected final TowerContainer tc;
	private final int spriteX;	// draw offset
	private final int spriteY;	// draw offset
	
	public GameTower(RunningGame rg, TowerType tt, int posX, int posY)
	{
		super(tt.getDamage(), tt.getRange(), tt.getSpeed(), tt.getSize(),
				tt.getCost(), posX, posY);
		this.tt = tt;
		this.sprite = rg.getAssetLoader().getSprite(tt);
		this.rg = rg;
		this.tc = rg.getTowerContainer();
		this.spriteX = tt.getSpriteWidth() / 2;
		this.spriteY = tt.getSpriteHeight() / 2;
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(sprite, getX() - spriteX, getY() - spriteY, null);
	}

	public TowerType getType() {return tt;}
}
