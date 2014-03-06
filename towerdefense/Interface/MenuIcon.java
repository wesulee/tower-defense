package towerdefense.Interface;

import java.awt.image.BufferedImage;

import towerdefense.towers.TowerType;

/**
 * Icon used for menu tower selection.
 */
public class MenuIcon extends Icon
{
	private final TowerType tt;
	
	public MenuIcon(BufferedImage img, int pos_x, int pos_y,
			TowerType tt)
	{
		super(img, pos_x, pos_y);
		this.tt = tt;
	}
	
	public TowerType getTowerType() {return tt;}
}
