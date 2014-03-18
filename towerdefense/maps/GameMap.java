package towerdefense.maps;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import towerdefense.AssetLoader;

/**
 * Map is used only to contain background sprite and appropriate information
 * for creatures to traverse map and where towers can be built.
 */
public class GameMap
{
	private final BufferedImage backgroundSprite;
	// creature's rectangle must always be within creaturePath
	private final Rectangle[] creaturePath;
	// where creature's spawn and then move inside creaturePath
	private final Rectangle spawnRect;
	// coordinates for guiding creature through map
	private final int[] pathX;
	private final int[] pathY;
	// maximum distance allowed from (pathX[i], pathY[i]) to consider
	// creature reaching the point
	private final int[] pathDistance;
	
	public GameMap(MapType mt)
	{
		String fileName = mt.getFileName();
		backgroundSprite = AssetLoader.loadMap(fileName);
		
		MapParser parser = new MapParser(mt);
		if (!parser.isValid())
			mapDataError(parser);
		
		creaturePath = parser.getCreaturePath();
		spawnRect = parser.getSpawnRect();
		pathX = parser.getPathX();
		pathY = parser.getPathY();
		pathDistance = parser.getPathDistance();
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
		for (int i = 0; i < creaturePath.length; i++)
			if (creaturePath[i].intersects(r))
				return false;
		return true;
	}
	
	private void mapDataError(MapParser parser)
	{
		System.out.print("Error reading map data: ");
		System.out.println(parser.getError() + ".");
		System.exit(1);
	}
}
