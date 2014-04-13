package towerdefense.creatures;

import java.awt.image.BufferedImage;

import towerdefense.components.AssetLoader;

public class CreatureSpider extends GameCreature
{
	private static final CreatureType ct = CreatureType.Spider;
	private static final BufferedImage[] sprites = 
			AssetLoader.loadCreature(ct);
	
	public CreatureSpider(double pos_x, double pos_y)
	{
		super(ct, pos_x, pos_y, sprites);
	}
}
