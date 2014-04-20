package towerdefense.components;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import towerdefense.GamePanel;
import towerdefense.TowerDefense;
import towerdefense.creatures.CreatureType;
import towerdefense.maps.MapType;
import towerdefense.projectiles.Fireball;
import towerdefense.projectiles.Frost;
import towerdefense.towers.TowerType;
import towerdefense.util.Utility;

public final class AssetLoader
{
	private static final boolean printFailure = true;
	private static final boolean exitOnFailure = true;
	private final EnumMap<TowerType, BufferedImage> tSpriteMap;
	private final HashMap<String, BufferedImage> assets;
	private final GamePanel gp;
	private String validationErrorMsg = null;
	
	public AssetLoader(GamePanel gp)
	{
		this.gp = gp;
		// initialize tower sprites
		this.tSpriteMap = new EnumMap<TowerType, BufferedImage>(TowerType.class);
		for (TowerType tt : TowerType.values()) {
			BufferedImage img = loadSprite(tt);
			if (img != null) tSpriteMap.put(tt, img);
		}
		this.assets = new HashMap<String, BufferedImage>();
	}
	
	// returns true if all game assets exist
	public boolean validateAssets()
	{
		for (String path : getAllAssetsPath())
			if (!validPath(path)) {
				validationErrorMsg = "Asset validation error: " + path;
				return false;
			}
		
		return true;
	}
	
	public LinkedList<String> getAllAssetsPath()
	{
		LinkedList<String> paths = new LinkedList<String>();
		
		// towers
		for (TowerType tt : TowerType.values()) {
			paths.add(tt.getPath());
			paths.add(Utility.toIconPath(tt.getPath()));
		}
		// maps
		for (MapType mt : MapType.values()) {
			paths.add(mt.getPath());
			paths.add(mt.getDataPath());
		}
		// creatures
		for (CreatureType ct : CreatureType.values())
			for (String path : Utility.creaturePaths(ct))
				paths.add(path);
		// projectiles
		paths.add(Fireball.path1);
		paths.add(Fireball.path2);
		paths.add(Frost.path1);
		paths.add(Frost.path2);
		
		return paths;
	}
	
	private boolean validPath(String path)
	{
		if (path == null) {
			gp.warningDialog( "Resource Validation Warning",
					"path should not be null!");
		}
		
		path = "Resources/" + path;
		try {
			InputStream is = TowerDefense.class.getResourceAsStream(path);
			return is != null;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public BufferedImage getSprite(TowerType tt)
	{
		return tSpriteMap.get(tt);
	}
	
	public static BufferedImage getSpriteIcon(TowerType tt)
	{
		String fname = Utility.toIconPath(tt.getPath());
		return loadResource(fname);
	}
	
	// initial sprite loading
	private static BufferedImage loadSprite(TowerType tt)
	{
		return loadResource(tt.getPath());
	}
	
	// path should exclude "Resources/"
	public static BufferedImage loadResource(String path)
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
	
	public BufferedImage getResource(String path)
	{
		path = "Resources/" + path;
		BufferedImage img = null;
		try {
			InputStream is = TowerDefense.class.getResourceAsStream(path);
			img = ImageIO.read(is);
		} 
		catch (Exception e) {
			gp.errorDialog("IO Error", "Unable to load " + path);
		}
		return img;
	}
	
	public static BufferedImage[] loadCreature(CreatureType ct)
	{
		BufferedImage[] images = new BufferedImage[4];
		int i = 0;
		
		for (String path : Utility.creaturePaths(ct))
			images[i++] = loadResource(path);
		
		return images;
	}
	
	public BufferedImage get(String path, boolean cache)
	{
		if (assets.containsKey(path))
			return assets.get(path);
		
		BufferedImage img = getResource(path);
		if (cache)
			assets.put(path, img);
		
		return img;
	}
	
	public BufferedImage get(String path)
	{
		return get(path, false);
	}
	
	public void unloadMapsExcept(MapType mt)
	{
		for (MapType m : MapType.values())
			if (m != mt && assets.containsKey(m.getPath()))
				assets.remove(m.getPath());
	}
	
	public String getValidationErrorMsg() {return validationErrorMsg;}
}
