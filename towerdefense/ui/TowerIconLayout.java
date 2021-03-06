package towerdefense.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import towerdefense.components.AssetLoader;
import towerdefense.towers.TowerType;
import towerdefense.util.Utility;

public class TowerIconLayout
{
	private final Menu menu;
	private final GenericIconGridLayout<TowerType> layout;
	// icon to draw when disabled
	private final Icon[] disabledIcons;
	// index of icon to draw
	private final boolean[] enabled;
	private final int[] cost;
	// change cursor to hand even when icon is disabled?
	private final boolean handCursorOverDisabled = false;
	
	public TowerIconLayout(Menu menu)
	{
		this.menu = menu;
		// initialize/populate grid layout
		int iconBitmapSize = menu.ICON_SIZE + menu.ICON_MARGIN * 2 + 2;
		int rowHeight = iconBitmapSize + menu.ICON_PADDING_Y * 2;
		int layoutWidth = menu.ICON_COLUMNS * iconBitmapSize +
				(menu.ICON_COLUMNS + 1) * menu.ICON_PADDING_X;
		int xStart = menu.X_START + (menu.WIDTH - layoutWidth) / 2;
		int yStart = menu.ICON_Y_START - menu.ICON_PADDING_Y;
		
		layout = new GenericIconGridLayout<TowerType>(xStart, yStart,
				layoutWidth, menu.ICON_COLUMNS, rowHeight,
				Alignment.Center, Alignment.Center);
		
		for (TowerType tt : TowerType.values()) {
			BufferedImage img = AssetLoader.getSpriteIcon(tt);
			img = iconize(img);
			if (!layout.add(img, tt)) {
				menu.getGamePanel().errorDialog("Layout Error",
						"Menu layout failed.");
			}
		}
		layout.finalize();
		
		final int drawSize = layout.size();
		enabled = new boolean[drawSize];
		Arrays.fill(enabled, true);
		
		// initialize disabled icons, cost
		disabledIcons = new Icon[drawSize];
		cost = new int[drawSize];
		for (int i = 0; i < drawSize; i++) {
			GenericIcon<TowerType> icon = layout.getIcon(i);
			BufferedImage img = Utility.toGrayscale(icon.getImage());
			Icon disabledIcon = new Icon(img, icon.getX(), icon.getY());
			disabledIcons[i] = disabledIcon;
			cost[i] = icon.getEntity().getCost();
		}
	}
	
	public void draw(final Graphics2D g)
	{
		for (int i = 0; i < enabled.length; i++)
			if (enabled[i])
				layout.drawIcon(g, i);
			else
				disabledIcons[i].draw(g);
	}
	
	public void recalcAvailable(final int gold)
	{
		for (int i = 0; i < cost.length; i++)
			enabled[i] = cost[i] <= gold;
	}
	
	public GenericIcon<TowerType> getIcon(final int x, final int y)
	{
		return layout.getIcon(x, y);
	}
	
	// is (x, y) inside an enabled icon?
	public boolean insideIcon(final int x, final int y)
	{
		if (handCursorOverDisabled)
			return layout.insideIcon(x, y);
		else {
			int i = layout.getIndex(layout.getRow(y), layout.getColumn(x));
			if (layout.validIndex(i) && enabled[i])
				return layout.getIcon(i).contains(x, y);
			else
				return false;
		}
	}
	
	public Rectangle getMinRect() {return layout.getMinRect();} 
	
	private BufferedImage iconize(BufferedImage img)
	{
		BufferedImage newImg = new BufferedImage(
				menu.ICON_SIZE + menu.ICON_MARGIN * 2,
				menu.ICON_SIZE + menu.ICON_MARGIN * 2,
				BufferedImage.TYPE_INT_RGB
		);
		Graphics2D g = newImg.createGraphics();
		g.setColor(menu.ICON_BG_COLOR);
		g.fillRect(0, 0, newImg.getWidth(), newImg.getHeight());
		g.drawImage(img, menu.ICON_MARGIN, menu.ICON_MARGIN, null);
		g.dispose();
		newImg = Utility.borderImage(newImg, menu.ICON_OUTLINE_COLOR, 1);
		return newImg;
	}
}
