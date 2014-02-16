package towerdefense;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.EnumMap;
import java.util.LinkedList;

import towerdefense.towers.TowerType;

/**
 * Menu layout in vertical order: wave, icons, description, health/gold
 */
public class Menu 
{
	private class MenuIcon {
		private final BufferedImage img;
		private final int pos_x;
		private final int pos_y;
		private final int size;
		private final TowerType tt;
		
		public MenuIcon(BufferedImage img, int pos_x, int pos_y,
				TowerType tt) {
			this.img = img;
			this.pos_x = pos_x;
			this.pos_y = pos_y;
			this.size = img.getHeight();
			this.tt = tt;
		}
		public BufferedImage getImage() {return createCopy(img);}
		public int getX() {return pos_x;}
		public int getY() {return pos_y;}
		public TowerType getTowerType() {return tt;}
		public void draw(Graphics2D g) {g.drawImage(img, pos_x, pos_y, null);}
		public boolean contains(int x, int y) {
			return ((x >= pos_x) && (x <= pos_x + size) && 
					(y >= pos_y) && (y <= pos_y + size));
		}
	}
	private class DescriptionLayout {
		private Font font = new Font("SansSerif", Font.BOLD, 12);
		private final float drawX;
		private final float drawWidth;
		private final float drawYStart;
		private LineBreakMeasurer lineMeasurer;
		private AttributedString description;
		private int paragraphStart;
		private int paragraphEnd;
		private LinkedList<TextLayout> layouts;
		
		public DescriptionLayout(String text, int x1, int x2, int y) {
			this.description = new AttributedString(text);
			description.addAttribute(TextAttribute.FONT, font);
			this.drawX = (float)x1;
			this.drawWidth = (float)(x2 - x1);
			this.drawYStart = (float)y;
			layouts = new LinkedList<TextLayout>();
		}
		
		public void draw(Graphics2D g) {
			float drawY = drawYStart;
			
			if (lineMeasurer == null) {
				AttributedCharacterIterator paragraph = 
						description.getIterator();
				paragraphStart = paragraph.getBeginIndex();
				paragraphEnd = paragraph.getEndIndex();
				FontRenderContext frc = g.getFontRenderContext();
				lineMeasurer = new LineBreakMeasurer(paragraph, frc);
				
				lineMeasurer.setPosition(paragraphStart);
				while (lineMeasurer.getPosition() < paragraphEnd) {
					TextLayout layout = lineMeasurer.nextLayout(drawWidth);
					layouts.add(layout);
					drawY += layout.getAscent();
					layout.draw(g, drawX, drawY);
					drawY += layout.getDescent() + layout.getLeading();
				}
			}
			else {
				lineMeasurer.setPosition(paragraphStart);
				for (TextLayout layout : layouts) {
					drawY += layout.getAscent();
					layout.draw(g, drawX, drawY);
					drawY += layout.getDescent() + layout.getLeading();
				}
			}
			
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
	
	private final GamePanel gp;
	private final Player player;
	private Font font;
	private final BufferedImage staticMenu;
	// type that mouse clicked on, activated only if sufficient funds
	private TowerType typeSelected = null;
	private final EnumMap<TowerType, DescriptionLayout> tDescMap;
	private DescriptionLayout descToDraw = null;
	
	private MenuIcon[] icons;
	private boolean[] availableTowers;
	
	public Menu(int width, int height, int width_offset, GamePanel gp, Player player)
	{
		WIDTH = width;
		HEIGHT = height;
		WIDTH_OFFSET = width_offset;
		this.gp = gp;
		this.player = player;
		font = new Font("SansSerif", Font.BOLD, 12);
		tDescMap = new EnumMap<TowerType, DescriptionLayout>(TowerType.class);
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
			tDescMap.put(tt, new DescriptionLayout(
					tt.getDescription(),DESC_X1, DESC_X2, DESC_Y1));
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
		
		g.drawString("Wave " + gp.getWaveController().getWaveNumber(),
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
			availableTowers[i] = (gold >= icons[i].getTowerType().getCost());
		}
	}
	
	public void notifyMouseMoved(int x, int y)
	{
		int i = getIconIndexSelected(x, y);
		if (i == -1) {
			if (gp.getCurrentCursor() != gp.getDefaultCursor())
				gp.setCurrentCursor(gp.getDefaultCursor());
		}
		else {
			if (gp.getCurrentCursor() != gp.getHandCursor())
				gp.setCurrentCursor(gp.getHandCursor());
		}
	}
	
	public void notifyMouseClicked(int x, int y)
	{
		int i = getIconIndexSelected(x, y);
		if (i == -1) {
			if (typeSelected != null) {
				gp.getTowerContainer().clearMenuSelectedTower();
			}
			typeSelected = null;
			return;
		}
		// only set typeSelected when player has money for tower
		// and tower is different from current typeSelected
		if ((icons[i].getTowerType().getCost() <= player.getGold()) &&
				(typeSelected != icons[i].getTowerType())) {
			typeSelected = icons[i].getTowerType();
			descToDraw = tDescMap.get(typeSelected);
			gp.getTowerContainer().setMenuSelectedTower(typeSelected);
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
	
	// return a clone of img
	private static BufferedImage createCopy(BufferedImage img)
	{
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
