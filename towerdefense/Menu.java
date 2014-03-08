package towerdefense;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.EnumMap;

import towerdefense.gamestates.RunningGame;
import towerdefense.towers.TowerType;
import towerdefense.ui.GenericIcon;
import towerdefense.ui.TextBox;

/**
 * Menu layout in vertical order: wave, icons, description, health/gold
 */
public class Menu 
{
	private class MenuIcon extends GenericIcon<TowerType> {
		MenuIcon(BufferedImage img, int pos_x, int pos_y, TowerType tt) {
			super(img, pos_x, pos_y, tt);
		}
	}
	private final int WIDTH;
	private final int HEIGHT;
	private final int WIDTH_OFFSET;
	private final Color BG_COLOR = Color.gray;
	// distance from top of where icons will begin
	private final int ICON_Y_START = 50;
	// padding between icons on same row
	private final int ICON_PADDING_X = 2;
	// padding below an icon
	private final int ICON_PADDING_Y = 2;
	// size of the local icon bitmap
	private final int ICON_SIZE = 30;
	private final int ICON_MARGIN = 3;
	// total space that an icon takes up
	private final int ICON_TOTAL_SIZE;
	private final int ICON_COLUMNS = 3;
	private final int ICON_COUNT;	// number of icons
	private final Color ICON_BG_COLOR = Color.white;
	private final Color ICON_OUTLINE_COLOR = Color.black;
	// positions of rectangle that description will be drawn
	private final int DESC_X1;
	private final int DESC_X2;
	private final int DESC_Y1;
	
	// constants for mouse movement
	// coordinates for rectangle that icons are contained in
	private final int MOUSE_X1;
	private final int MOUSE_X2;
	private final int MOUSE_Y1;
	private final int MOUSE_Y2;
	
	private final RunningGame rg;
	private final Player player;
	private Font font;
	private final BufferedImage staticMenu;
	// type that mouse clicked on, activated only if sufficient funds
	private TowerType typeSelected = null;
	private final EnumMap<TowerType, TextBox> tDescMap;
	private TextBox descToDraw = null;
	
	private MenuIcon[] icons;
	private boolean[] availableTowers;
	
	public Menu(int width, int height, int width_offset, RunningGame rg, Player player)
	{
		WIDTH = width;
		HEIGHT = height;
		WIDTH_OFFSET = width_offset;
		this.rg = rg;
		this.player = player;
		font = new Font("SansSerif", Font.BOLD, 12);
		tDescMap = new EnumMap<TowerType, TextBox>(TowerType.class);
		ICON_TOTAL_SIZE = ICON_SIZE + (ICON_MARGIN * 2) + 2;
		ICON_COUNT = TowerType.values().length;
		DESC_X1 = WIDTH_OFFSET + 5;
		DESC_X2 = WIDTH - 5;
		DESC_Y1 = 200;
		
		// initialize variables for icon position
		int x = WIDTH_OFFSET + (WIDTH - WIDTH_OFFSET - 
				(ICON_TOTAL_SIZE * ICON_COLUMNS + ICON_PADDING_X * (ICON_COLUMNS - 1))) / 2;
		int dx = ICON_TOTAL_SIZE + ICON_PADDING_X;
		int y = ICON_Y_START;
		int dy = ICON_TOTAL_SIZE + ICON_PADDING_Y;
		// initialize icons
		int i = 0;
		int column = 1;
		icons = new MenuIcon[ICON_COUNT];
		for (TowerType tt : TowerType.values()) {
			tDescMap.put(tt, new TextBox(
					tt.getDescription(), DESC_X1, DESC_X2, DESC_Y1));
			BufferedImage sprite = SpriteContainer.getSpriteIcon(tt);
			if (sprite == null) {
				System.exit(1);
			}
			sprite = createIconImage(sprite);
			MenuIcon icon = new MenuIcon(sprite, x, y, tt);
			icons[i] = icon;
			if (column != ICON_COLUMNS) {
				x += dx;
				column++;
			}
			else {
				x -= dx * (ICON_COLUMNS - 1);
				y += dy;
				column = 1;
			}
			i++;
		}
		
		staticMenu = createStaticMenuImage();
		
		availableTowers = new boolean[ICON_COUNT];
		for (i = 0; i < ICON_COUNT; i++)
			availableTowers[i] = false;
		
		MOUSE_X1 = icons[0].getX();
		MOUSE_X2 = MOUSE_X1 + ICON_TOTAL_SIZE * ICON_COLUMNS + 
				ICON_PADDING_X * (ICON_COLUMNS - 1);
		MOUSE_Y1 = ICON_Y_START;
		MOUSE_Y2 = MOUSE_Y1 + (ICON_TOTAL_SIZE + ICON_PADDING_Y) * 
				(ICON_COUNT / ICON_COLUMNS + 1) - ICON_PADDING_Y;
	}
	
	public void draw(Graphics2D g)
	{
		int gold = player.getGold();
		
		g.drawImage(staticMenu, WIDTH_OFFSET, 0, null);
		
		g.setFont(font);
		g.setColor(Color.white);
		
		g.drawString("Wave " + rg.getWaveController().getWaveNumber(),
				WIDTH_OFFSET + 20, 20);
		
		// display health and gold
		g.drawString("Health: " + player.getHealth(),
				WIDTH_OFFSET + 15, HEIGHT - 45);
		g.drawString("Gold: " + gold, WIDTH_OFFSET + 15, HEIGHT - 25);
		
		for (int i = 0; i < ICON_COUNT; i++) {
			if (availableTowers[i])
				icons[i].draw(g);
		}
		
		if (descToDraw != null)
			descToDraw.draw(g);
	}
	
	public TowerType getSelectedTower() {return typeSelected;}
	
	// the player's gold has been changed, update available towers
	public void notifyGoldChange()
	{
		int gold = player.getGold();
		for (int i = 0; i < ICON_COUNT; i++) {
			availableTowers[i] = (gold >= icons[i].getEntity().getCost());
		}
	}
	
	public void notifyMouseMoved(int x, int y)
	{
		int i = getIconIndexSelected(x, y);
		if (i == -1) {
			rg.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		}
		else {
			rg.setCurrentCursor(Cursor.HAND_CURSOR);
		}
	}
	
	public void notifyMouseClicked(int x, int y)
	{
		int i = getIconIndexSelected(x, y);
		if (i == -1) {
			if (typeSelected != null) {
				rg.getTowerContainer().clearMenuSelectedTower();
			}
			typeSelected = null;
			return;
		}
		// only set typeSelected when player has money for tower
		// and tower is different from current typeSelected
		if ((icons[i].getEntity().getCost() <= player.getGold()) &&
				(typeSelected != icons[i].getEntity())) {
			typeSelected = icons[i].getEntity();
			descToDraw = tDescMap.get(typeSelected);
			rg.getTowerContainer().setMenuSelectedTower(typeSelected);
		}
	}
	
	// is a tower currently selected from menu?
	public boolean towerIsSelected() {return typeSelected != null;}
	
	// player is no longer selecting a tower
	public void clearSelectedTower() {typeSelected = null;}
	
	// returns index of icon at (x, y) if exists, else -1
	private int getIconIndexSelected(int x, int y)
	{
		// is (x, y) inside icon rectangle?
		if ((y < MOUSE_Y1) || (y > MOUSE_Y2) || (x < MOUSE_X1) ||
				(x > MOUSE_X2))
			return -1;
		
		// row/column number starts at 0
		int column = (x - MOUSE_X1) / (ICON_TOTAL_SIZE + ICON_PADDING_X);
		int row = (y - MOUSE_Y1) / (ICON_TOTAL_SIZE + ICON_PADDING_Y);
		int i = row * ICON_COLUMNS + column;
		
		if ((i > ICON_COUNT - 1) || !icons[i].contains(x, y))
			return -1;
		else
			return i;
	}
	
	private BufferedImage createIconImage(BufferedImage sprite)
	{
		BufferedImage img = new BufferedImage(ICON_TOTAL_SIZE, ICON_TOTAL_SIZE,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		// create outline and background
		g.setColor(ICON_OUTLINE_COLOR);
		g.fillRect(0, 0, ICON_TOTAL_SIZE, ICON_TOTAL_SIZE);
		g.setColor(ICON_BG_COLOR);
		g.fillRect(1, 1, ICON_TOTAL_SIZE-2, ICON_TOTAL_SIZE-2);
		
		int offset = (ICON_TOTAL_SIZE - ICON_SIZE) / 2;
		g.drawImage(sprite, offset, offset, null);
		
		g.dispose();
		return img;
	}
	
	private BufferedImage createStaticMenuImage()
	{
		int width = WIDTH - WIDTH_OFFSET;
		BufferedImage simg = new BufferedImage(width, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = simg.createGraphics();
		
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, width, HEIGHT);
		
		for (MenuIcon icon : icons) {
			BufferedImage img = icon.getImage();
			ColorConvertOp op = 
					new ColorConvertOp(ColorSpace.getInstance(
							ColorSpace.CS_GRAY), null);
			op.filter(img, img);
			
			g.drawImage(img, icon.getX() - WIDTH_OFFSET, icon.getY(), null);
		}
		
		return simg;
	}
}
