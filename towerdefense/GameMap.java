package towerdefense;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Map is used only to contain background sprite and appropriate information
 * for creatures to traverse map and where towers can be built.
 */
public class GameMap
{
	private final GamePanel gp;
	private final String fileName = "test_map.png";
	private final BufferedImage backgroundSprite;
	// creature's rectangle must always be within creaturePath
	private final Rectangle creaturePath;
	// where creature's spawn and then move inside creaturePath
	private final Rectangle spawnRect;
	// coordinates for guiding creature through map
	private final int[] pathX;
	private final int[] pathY;
	// maximum distance allowed from (pathX[i], pathY[i]) to consider
	// creature reaching the point
	private final int[] pathDistance;
	private final int CREATURE_END_INDEX;
	
	public GameMap(GamePanel gp)
	{
		this.gp = gp;
		backgroundSprite = SpriteContainer.loadMap(fileName);
		
		creaturePath = new Rectangle(-50, 273, 900, 54);
		spawnRect = new Rectangle(800, 273, 50, 54);
		pathX = new int[]{0};
		pathY = new int[]{300};
		pathDistance = new int[]{5};
		CREATURE_END_INDEX = pathX.length;
	}
	
	public Rectangle getSpawnRectangle() {return spawnRect;}
	public int getPointsLength() {return pathX.length;}
	public int getPathX(int i) {return pathX[i];}
	public int getPathY(int i) {return pathY[i];}
	public int getPathDistance(int i) {return pathDistance[i];}
		
	public void draw(Graphics2D g)
	{
		g.drawImage(backgroundSprite, 0, 0, null);
	}
	
	// can a tower be built at given location?
	// (x, y) should be position of center of tower
	public boolean spotAvailable(Rectangle r)
	{
		return !creaturePath.intersects(r);
	}
}
