package towerdefense.gamestates;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import towerdefense.AssetLoader;
import towerdefense.GamePanel;
import towerdefense.Utility;
import towerdefense.maps.MapType;
import towerdefense.ui.Alignment;
import towerdefense.ui.GenericIconGridLayout;

public class MapSelector extends BasicGameState
{
	// map icon must be contained in a rectangle of this size
	private final int iconRectangleX = 120;
	private final int iconRectangleY = 100;
	private final int iconsPerRow = 3;
	// vertical padding between rows
	private final int iconPaddingY = 15;
	// padding on sides of layout
	private final int xPadding = 25;
	// distance from top to start icons
	private final int iconYOffset = 100;
	
	private final GenericIconGridLayout<MapType> layout;
	private boolean mapSelected = false;
	private MapType selectedMap;
	private final AssetLoader al;
	
	public MapSelector(GamePanel gp, LoadingScreen ls)
	{
		super(gp, 500, 5.0);
		al = ls.getAssets();
		layout = new GenericIconGridLayout<MapType>(xPadding, iconYOffset,
				GamePanel.WIDTH - xPadding*2, iconsPerRow,
				iconRectangleY + iconPaddingY, Alignment.Center,
				Alignment.Center);		
		
		for (MapType mt : MapType.values()) {
			BufferedImage img = al.get(mt.getPath());
			BufferedImage resized = Utility.scaledResize(img, 
					iconRectangleX, iconRectangleY);
			layout.add(resized, mt);
		}
		layout.finalize();
	}

	public boolean update(long time)
	{
		return mapSelected;
	}

	public void draw(Graphics2D g)
	{
		layout.draw(g);
	}
	
	public void mouseMoved(int x, int y)
	{
		if (layout.contains(x, y) && layout.insideIcon(x, y))
			setCurrentCursor(Cursor.HAND_CURSOR);
		else
			setCurrentCursor(Cursor.DEFAULT_CURSOR);
	}

	public void mouseClicked(int x, int y)
	{
		if (layout.contains(x, y) && layout.insideIcon(x, y)) {
			selectedMap = layout.getIcon(x, y).getEntity();
			mapSelected = true;
		}
	}
	
	public void processKey(KeyEvent e) {}
	
	public GameState transition()
	{
		return new RunningGame(getGamePanel(), selectedMap, al);
	}
}
