package towerdefense.maps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import towerdefense.TowerDefense;
import towerdefense.gamestates.RunningGame;
import towerdefense.util.Utility;

/**
 * Map is used only to contain background sprite and appropriate information
 * for creatures to traverse map and where towers can be built.
 */
public class GameMap
{
	private final RunningGame rg;
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
	
	public GameMap(RunningGame rg, MapType mt)
	{
		this.rg = rg;
		
		MapParser parser = new MapParser(mt);
		if (!parser.isValid())
			mapDataError(parser);
		
		this.creaturePath = parser.getCreaturePath();
		this.spawnRect = parser.getSpawnRect();
		this.pathX = parser.getPathX();
		this.pathY = parser.getPathY();
		this.pathDistance = parser.getPathDistance();
		if (TowerDefense.DEBUG)
			this.backgroundSprite =
			debugMapImage(rg.getAssetLoader().get(mt.getPath()));
		else
			this.backgroundSprite = rg.getAssetLoader().get(mt.getPath());
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
		rg.getGamePanel().errorDialog("Map Data Error", 
				parser.getError() + ".");
	}
	
	private BufferedImage debugMapImage(BufferedImage img)
	{
		final BufferedImage retImg = Utility.createCopy(img);
		final Color[] pathColor = {Color.red, Color.orange};
		int pathColorIndex = 0;
		final Graphics2D g = (Graphics2D) retImg.getGraphics();
		
		
		// draw creature path
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.1f));
		for (Rectangle r : creaturePath) {
			g.setColor(pathColor[pathColorIndex]);
			g.fill(r);
			pathColorIndex = (pathColorIndex + 1) % pathColor.length;
		}
		
		// draw path distance circles
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f));
		g.setColor(Color.cyan);
		for (int i = 0; i < pathX.length; i++) {
			Ellipse2D.Float circ = new Ellipse2D.Float(
					pathX[i], pathY[i], pathDistance[i], pathDistance[i]);
			g.fill(circ);
		}
		
		// draw checkpoints
		g.setColor(Color.white);
		for (int i = 0; i < pathX.length; i++) {
			g.drawLine(pathX[i]-2, pathY[i], pathX[i]+2, pathY[i]); // horiz line
			g.drawLine(pathX[i], pathY[i]-2, pathX[i], pathY[i]+2); // vert line
		}
		
		g.dispose();
		return retImg;
	}
}
