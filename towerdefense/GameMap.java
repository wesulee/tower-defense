package towerdefense;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

/**
 * Map is used only to contain background sprite and appropriate information
 * for creatures to traverse map and where towers can be built.
 */
public class GameMap
{
	private final GamePanel gp;
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
	
	public GameMap(GamePanel gp, String fileName)
	{
		this.gp = gp;
		backgroundSprite = SpriteContainer.loadMap(fileName);
		
		String data = Utility.readStrFromFile(
				"Maps/" + Utility.excludeExt(fileName) + ".txt");
		if (data.isEmpty()) 
			mapDataError("file could not be read");
		
		int str_i = 0;
		String tmp;
		// read creaturePath rectangles
		tmp = data.substring(str_i, data.indexOf('\n'));
		str_i += tmp.length() + 1;
		int creaturePathRectCount = Integer.parseInt(tmp);
		creaturePath = new Rectangle[creaturePathRectCount];
		for (int i = 0; i < creaturePathRectCount; i++) {
			tmp = data.substring(str_i, data.indexOf('\n', str_i));
			str_i += tmp.length() + 1;
			int[] ints = Utility.getInts(tmp);
			if (ints.length != 4)
				mapDataError("creaturePath incorrect");
			creaturePath[i] = new Rectangle(
					ints[0], ints[1], ints[2], ints[3]
			);
		}
		str_i++;
		// read spawnRect rectangle
		tmp = data.substring(str_i, data.indexOf('\n', str_i));
		str_i += tmp.length() + 2;
		int[] ints = Utility.getInts(tmp);
		if (ints.length != 4)
			mapDataError("spawnRect incorrect");
		spawnRect = new Rectangle(ints[0], ints[1], ints[2], ints[3]);
		// read creature path points
		tmp = data.substring(str_i, data.indexOf('\n', str_i));
		str_i += tmp.length() + 1;
		int pathCount = Integer.parseInt(tmp);
		pathX = new int[pathCount];
		pathY = new int[pathCount];
		pathDistance = new int[pathCount];
		for (int i = 0; i < pathCount; i++) {
			tmp = data.substring(str_i, data.indexOf('\n', str_i));
			str_i += tmp.length() + 1;
			ints = Utility.getInts(tmp);
			if (ints.length != 3)
				mapDataError("creature path points incorrect");
			pathX[i] = ints[0];
			pathY[i] = ints[1];
			pathDistance[i] = ints[2];
		}
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
	
	private void mapDataError(String msg)
	{
		System.out.println("Error reading map data: " + msg + '.');
		System.exit(1);
	}
}
