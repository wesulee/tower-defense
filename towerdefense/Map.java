package towerdefense;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Map is used only to contain Wave data and the background sprite.
 */
public class Map
{
	private final GamePanel gp;
	private BufferedImage backgroundSprite;
	private final Rectangle creaturePath;
	private final Rectangle spawnRect;
	
	public Map(GamePanel gp)
	{
		this.gp = gp;
		String fname = "Resources/Maps/test_map.png";
		try {
			InputStream is = TowerDefense.class.getResourceAsStream(fname);
			backgroundSprite = ImageIO.read(is);
		} 
		catch (Exception e) {
			System.out.println("Unable to load " + fname);
			System.exit(1);
		}
		
		creaturePath = new Rectangle(-50, 273, 900, 54);
		spawnRect = new Rectangle(800, 273, 50, 54);
	}
	
	public Rectangle getSpawnRectangle() {return spawnRect;}
		
	public void draw(Graphics2D g)
	{
		g.drawImage(backgroundSprite, 0, 0, null);
	}
	
	// can a tower be built at given location?
	// (x, y) should be position of center of tower
	public boolean spotAvailableForTower(Rectangle r)
	{
		return !creaturePath.intersects(r);
	}
}
