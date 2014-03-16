package towerdefense;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.EnumMap;

import javax.imageio.ImageIO;

import towerdefense.creatures.CreatureType;
import towerdefense.towers.TowerType;

public class SpriteContainer
{
	private static final boolean printFailure = true;
	private static final boolean exitOnFailure = true;
	private static EnumMap<TowerType, BufferedImage> tSpriteMap;
	
	public SpriteContainer()
	{
		// initialize tower sprites
		tSpriteMap = new EnumMap<TowerType, BufferedImage>(TowerType.class);
		for (TowerType tt : TowerType.values()) {
			BufferedImage img = loadSprite(tt);
			if (img != null) tSpriteMap.put(tt, img);
		}
	}
	
	public static BufferedImage getSprite(TowerType tt)
	{
		return tSpriteMap.get(tt);
	}
	
	public static BufferedImage getSpriteIcon(TowerType tt)
	{
		String fname = "Towers/" + tt.getSpriteName();
		int extensionIndex = fname.lastIndexOf(".");
		String extension = fname.substring(extensionIndex + 1);
		fname = fname.substring(0, extensionIndex) + "_icon." + extension;
		BufferedImage img = loadResource(fname);
		return img;
	}
	
	// initial sprite loading
	private static BufferedImage loadSprite(TowerType tt)
	{
		String path = "Towers/" + tt.getSpriteName();
		return loadResource(path);
	}
	
	// path should exclude "Resources/"
	private static BufferedImage loadResource(String path)
	{
		path = "Resources/" + path;
		BufferedImage img = null;
		try {
			InputStream is = TowerDefense.class.getResourceAsStream(path);
			img = ImageIO.read(is);
		} 
		catch (Exception e) {
			if (printFailure)
				System.out.println("Unable to load " + path);
			if (exitOnFailure)
				System.exit(1);
		}
		return img;
	}
	
	public static BufferedImage loadMap(String fname)
	{
		String path = "Maps/" + fname;
		return loadResource(path);
	}
	
	public static BufferedImage[] loadCreature(CreatureType ct)
	{
		BufferedImage[] images = new BufferedImage[4];
		String basePath = "Creatures/" + ct.getSpriteName();
		for (Direction d : Direction.values()) {
			String path = basePath + d.toSuffix() + ct.getSpriteExtension();
			images[d.toIndex()] = loadResource(path);
		}
		return images;
	}
	
	public static BufferedImage loadProjectile(String name) {
		BufferedImage img = loadResource("Projectiles/" + name);
		return img;
	}
}
