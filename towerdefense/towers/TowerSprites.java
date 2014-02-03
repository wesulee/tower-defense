package towerdefense.towers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.io.InputStream;

import towerdefense.TowerDefense;

/**
 * Container for tower sprites and their icons
 */
public class TowerSprites
{
	private static ArrayList<BufferedImage> sprites;
	
	public TowerSprites()
	{
		sprites = new ArrayList<BufferedImage>();
		
		for (TowerType tt : TowerType.values()) {
			BufferedImage img = loadSprite(tt.getFileName());
			if (img == null) {
				// unable to load tower sprite...exit
				System.exit(1);
			}
			else {
				sprites.add(img);
			}
		}
	}
	
	public static BufferedImage getSprite(TowerType tt)
	{
		int i = 0;
		for (TowerType _tt : TowerType.values()) {
			if (tt == _tt) {
				return sprites.get(i);
			}
			i++;
		}
		return null;
	}
	
	public static BufferedImage getSpriteIcon(TowerType tt)
	{
		String fname = tt.getFileName();
		int extensionIndex = fname.lastIndexOf(".");
		String extension = fname.substring(extensionIndex + 1);
		fname = fname.substring(0, extensionIndex) + "_icon." + extension;
		BufferedImage img = loadSprite(fname);
		return img;
	}
	
	// initial sprite loading
	private static BufferedImage loadSprite(String fname)
	{
		BufferedImage img = null;
		fname = "Resources/Towers/" + fname;
		try {
			InputStream is = TowerDefense.class.getResourceAsStream(fname);
			img = ImageIO.read(is);
		} 
		catch (Exception e) {
			System.out.println("Unable to load " + fname);
		}
		return img;
	}
}
