package towerdefense;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import towerdefense.towers.TowerSprites;
import towerdefense.towers.TowerType;

/**
 * Icon layout: 2 per row, centered horizontally.
 */
public class Menu 
{
	private class MenuIcon {
		private final BufferedImage img;
		private final int pos_x;
		private final int pos_y;
		private final int size;
		
		private MenuIcon(BufferedImage img, int pos_x, int pos_y) {
			this.img = img;
			this.pos_x = pos_x;
			this.pos_y = pos_y;
			this.size = img.getHeight();
		}
		public BufferedImage getImage() {return createCopy(img);}
		public int getX() {return pos_x;}
		public int getY() {return pos_y;}
		public void draw(Graphics2D g) {g.drawImage(img, pos_x, pos_y, null);}
		public boolean contains(int x, int y) {
			return ((x >= pos_x) && (x <= pos_x + size) && 
					(y >= pos_y) && (y <= pos_y + size));
		}
	}
	private final int WIDTH;
	private final int HEIGHT;
	private final int WIDTH_OFFSET;
	private final Color BG_COLOR = Color.gray;
	// distance from top of where icons will begin
	private final int ICON_Y_START = 100;
	// padding between icons on same row
	private final int ICON_PADDING_X = 5;
	// padding below an icon
	private final int ICON_PADDING_Y = 5;
	// size of the local icon bitmap
	private final int ICON_SIZE = 30;
	private final int ICON_MARGIN = 5;
	// total space that an icon takes up
	private final int ICON_TOTAL_SIZE;
	private final Color ICON_BG_COLOR = Color.white;
	private final Color ICON_OUTLINE_COLOR = Color.black;
	
	// constants for mouse movement
	private final int MOUSE_C1_X1;	// column 1, left edge
	private final int MOUSE_C1_X2;	// column 1, right edge
	private final int MOUSE_C2_X1;
	private final int MOUSE_C2_X2;
	private final int MOUSE_Y;
	private final int MOUSE_DY;
	
	private final Player player;
	private final int playerMaxHealth;
	private Font font;
	private int wave = 0;
	private final BufferedImage staticMenu;
	private MenuIcon selectedIcon = null;
	
	//private ArrayList<MenuIcon> icons;
	private MenuIcon[] icons;
	
	public Menu(int width, int height, int width_offset, Player player)
	{
		WIDTH = width;
		HEIGHT = height;
		WIDTH_OFFSET = width_offset;
		this.player = player;
		playerMaxHealth = player.getMaxHealth();
		font = new Font("SansSerif", Font.BOLD, 12);
		ICON_TOTAL_SIZE = ICON_SIZE + (ICON_MARGIN * 2) + 2;
		
		// initialize variables for icon position
		int x = WIDTH_OFFSET + (WIDTH - WIDTH_OFFSET - 
				(ICON_TOTAL_SIZE * 2 + ICON_PADDING_X)) / 2;
		int dx = ICON_TOTAL_SIZE + ICON_PADDING_X;
		int y = ICON_Y_START;
		int dy = ICON_TOTAL_SIZE + ICON_PADDING_Y;
		// initialize icons
		int i = 0;
		//icons = new ArrayList<MenuIcon>();
		icons = new MenuIcon[TowerType.values().length];
		for (TowerType tt : TowerType.values()) {
			BufferedImage sprite = TowerSprites.getSpriteIcon(tt);
			if (sprite == null) {
				System.exit(1);
			}
			sprite = createIconImage(sprite);
			MenuIcon icon = new MenuIcon(sprite, x, y);
			System.out.println(tt.toString() + " " + x + " " + y);
			icons[i] = icon;
			if (i % 2 == 0)
				x += dx;
			else {
				x -= dx;
				y += dy;
			}
			i++;
		}
		
		staticMenu = createStaticMenuImage();
		
		MOUSE_C1_X1 = icons[0].getX();
		MOUSE_C1_X2 = MOUSE_C1_X1 + ICON_TOTAL_SIZE;
		MOUSE_C2_X1 = icons[1].getX();
		MOUSE_C2_X2 = MOUSE_C2_X1 + ICON_TOTAL_SIZE;
		MOUSE_Y = ICON_Y_START;
		MOUSE_DY = ICON_TOTAL_SIZE + ICON_PADDING_Y;
	}
	
	public void draw(Graphics2D g)
	{
		int gold = player.getGold();
		
		g.drawImage(staticMenu, WIDTH_OFFSET, 0, null);
		
		g.setFont(font);
		g.setColor(Color.white);
		
		g.drawString("Wave " + wave, WIDTH_OFFSET + 20, 20);
		
		// display health and gold
		g.drawString("Health: " + player.getHealth() + "/" + playerMaxHealth, 
				WIDTH_OFFSET + 15, HEIGHT - 45);
		g.drawString("Gold: " + gold, WIDTH_OFFSET + 15, HEIGHT - 25);
		
		if (selectedIcon != null)
			selectedIcon.draw(g);
	}
	
	public void setWaveNumber(int n) {wave = n;}
	
	public void notifyMouseMoved(int x, int y)
	{
		if (y < MOUSE_Y) {
			selectedIcon = null;
			return;
		}
		else if ((x >= MOUSE_C1_X1) && (x <= MOUSE_C1_X2)) {
			int i = ((y - MOUSE_Y) / MOUSE_DY) * 2;
			if ((i >= icons.length) || !icons[i].contains(x, y)) {
				selectedIcon = null;
				return;
			}
			else
				selectedIcon = icons[i];
		}
		else if ((x >= MOUSE_C2_X1) && (x <= MOUSE_C2_X2)) {
			int i = ((y - MOUSE_Y) / MOUSE_DY) * 2 + 1;
			if ((i >= icons.length) || !icons[i].contains(x, y)) {
				selectedIcon = null;
				return;
			}
			else
				selectedIcon = icons[i];
		}
		else {
			selectedIcon = null;
			return;
		}
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
					new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
			op.filter(img, img);
			
			g.drawImage(img, icon.getX() - WIDTH_OFFSET, icon.getY(), null);
		}
		
		return simg;
	}
	
	// return a clone of img
	private static BufferedImage createCopy(BufferedImage img)
	{
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
