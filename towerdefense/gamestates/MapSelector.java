package towerdefense.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import towerdefense.GamePanel;
import towerdefense.SpriteContainer;
import towerdefense.Utility;
import towerdefense.maps.MapType;
import towerdefense.ui.GenericIcon;

public class MapSelector extends BasicGameState
{
	private class MapIcon extends GenericIcon<MapType> {
		MapIcon(BufferedImage img, int pos_x, int pos_y, MapType mt) {
			super(img, pos_x, pos_y, mt);
		}
	}
	// map icon must be contained in a rectangle of this size
	private final int iconRectangleX = 120;
	private final int iconRectangleY = 100;
	private final int iconsPerRow = 3;
	// vertical padding between rows
	private final int iconPaddingY = 15;
	// distance from top to start icons
	private final int iconYOffset = 100;
	
	private final int xOffset;
	private final int dx;
	private final int dy = iconPaddingY + iconRectangleY;
	private final MapIcon icons[];
	private boolean mapSelected = false;
	private MapType selectedMap;
	
	public MapSelector(GamePanel gp)
	{
		super(gp, 500, 5.0);
		icons = new MapIcon[MapType.values().length];
		
		int icon_dx = (GamePanel.WIDTH - ((iconsPerRow + 1) * iconRectangleX)) / iconsPerRow;
		xOffset = icon_dx;
		dx = iconRectangleX + xOffset;
		
		int i = 0;
		int column = 0;
		int row = 0;
		for (MapType mt : MapType.values()) {
			BufferedImage img = SpriteContainer.loadMap(mt.getFileName());
			BufferedImage resized = Utility.scaledResize(img, 
					iconRectangleX, iconRectangleY);
			int x = (column + 1) * icon_dx + column * iconRectangleX;
			x += (iconRectangleX - resized.getWidth()) / 2; // center x
			int y = iconYOffset + row * (iconRectangleY + iconPaddingY);
			y += (iconRectangleY - resized.getHeight()) / 2; // center y
			icons[i] = new MapIcon(resized, x, y, mt);
			i++;
			column = i % iconsPerRow;
			row = i / iconsPerRow;
		}
	}

	public boolean update(long time) {
		return mapSelected;
	}

	public void draw(Graphics2D g) {
		for (int i = 0; i < icons.length; i++)
			icons[i].draw(g);
	}
	
	public void mouseMoved(int x, int y) {}

	public void mouseClicked(int x, int y)
	{
		int column = (x - xOffset) / dx;
		int row = (y - iconYOffset) / dy;
		int i = row * iconsPerRow + column;
		if (i >= 0 && i < icons.length && icons[i].contains(x, y)) {
			selectedMap = icons[i].getEntity();
			mapSelected = true;
		}
	}
	
	public void processKey(KeyEvent e) {}
	
	public GameState transition()
	{
		return new RunningGame(getGamePanel(), selectedMap);
	}
}
