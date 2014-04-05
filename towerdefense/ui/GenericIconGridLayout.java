package towerdefense.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A basic grid layout for icons that supports Icon alignment.
 */
public class GenericIconGridLayout<T>
{
	// rectangle that completely contains layout
	private final Rectangle layoutRect;
	// smallest rectangle to contain all icons
	private final Rectangle minLayoutRect;
	private ArrayList<BufferedImage> imgList;
	private ArrayList<T> entityList;
	private Icon[] icons;
	private boolean enabled[];	// draw icons[i] only if enabled[i] == true
	private boolean finalized = false;
	private final int rowSize;	// icon count per row
	private final int rowHeight;
	private final int xStart;
	private final int yStart;
	private final int cellWidth;
	private final Alignment cellVertAlign;
	private final Alignment cellHorizAlign;
	
	public GenericIconGridLayout(int x, int y, int width, int rowSize,
			int rowHeight, Alignment cellVertAlign, Alignment cellHorizAlign)
	{
		this.rowSize = rowSize;
		this.rowHeight = rowHeight;
		this.xStart = x;
		this.yStart = y;
		this.cellWidth = width / rowSize;
		if ((cellVertAlign == Alignment.Top) ||
				(cellVertAlign == Alignment.Center) ||
				(cellVertAlign == Alignment.Bottom))
			this.cellVertAlign = cellVertAlign;
		else
			this.cellVertAlign = Alignment.Top; // default
		if ((cellHorizAlign == Alignment.Left) ||
				(cellHorizAlign == Alignment.Center) ||
				(cellHorizAlign == Alignment.Right))
			this.cellHorizAlign = cellHorizAlign;
		else
			this.cellHorizAlign = Alignment.Left; // default
		layoutRect = new Rectangle(x, y, width, rowHeight);
		minLayoutRect = new Rectangle(layoutRect);
		imgList = new ArrayList<BufferedImage>();
		entityList = new ArrayList<T>();
	}
	
	public void draw(Graphics2D g)
	{
		for (int i = 0; i < icons.length; i++)
			if (enabled[i]) icons[i].draw(g);
	}
	
	public boolean add(BufferedImage img, T entity)
	{
		if (finalized || (img.getWidth() > cellWidth) ||
				(img.getHeight() > rowHeight))
			return false;
		return (imgList.add(img) && entityList.add(entity));
	}
	
	// is (x, y) inside the smallest rect containing all icons?
	public boolean contains(int x, int y)
	{
		return minLayoutRect.contains(x, y);
	}
	
	// is (x, y) inside an icon?
	public boolean insideIcon(int x, int y)
	{
		int i = getIndex(getRow(y), getColumn(x));
		return (validIndex(i) && icons[i].contains(x, y));
	}
	
	// returns the icon at (x, y)
	@SuppressWarnings("unchecked")
	public GenericIcon<T> getIcon(int x, int y)
	{
		int i = getIndex(getRow(y), getColumn(x));
		if (validIndex(i))
			return (GenericIcon<T>) icons[i];
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public GenericIcon<T> getIcon(int index)
	{
		if (validIndex(index))
			return (GenericIcon<T>) icons[index];
		else
			return null;
	}
	
	// call once all icons have been added
	public void finalize()
	{
		enabled = new boolean[imgList.size()];
		for (int i = 0; i < enabled.length; i++)
			enabled[i] = true;
		
		BufferedImage img;
		T entity;
		icons = new Icon[imgList.size()];
		
		int row = 0;
		int column = 0;
		for (int i = 0; i < icons.length; i++) {
			img = imgList.get(i);
			entity = entityList.get(i);
			icons[i] = new GenericIcon<T>(img, getCellX(img, column), 
					getCellY(img, row), entity);
			row = (i + 1) / rowSize;
			column = (column + 1) % rowSize;
		}
		
		Rectangle rect = icons[0].getRect();
		int xMin = rect.x;
		int xMax = xMin + rect.width;
		int yMin = rect.y;
		int yMax = yMin + rect.height;
		for (int i = 1; i < icons.length; i++) {
			rect = icons[i].getRect();
			if (rect.x < xMin) xMin = rect.x;
			if (rect.y < yMin) yMin = rect.y;
			if (rect.x + rect.width > xMax) xMax = rect.x + rect.width;
			if (rect.y + rect.height > yMax) yMax = rect.y + rect.height;
		}
		
		minLayoutRect.setBounds(xMin, yMin, xMax - xMin, yMax - yMin);
		layoutRect.setBounds(layoutRect.x, layoutRect.y, layoutRect.width,
				minLayoutRect.y - layoutRect.y + minLayoutRect.height);
		finalized = true;
		imgList.clear();
		entityList.clear();
	}
	
	public void enable(int i) {if (validIndex(i)) enabled[i] = true;}
	public void disable(int i) {if (validIndex(i)) enabled[i] = false;}
	public Rectangle getRect() {return new Rectangle(layoutRect);}
	public Rectangle getMinRect() {return new Rectangle(minLayoutRect);}
	public int size() {return icons.length;}
	protected int getRow(int y) {return (y - yStart) / rowHeight;}
	protected int getColumn(int x) {return (x - xStart) / cellWidth;}
	protected int getIndex(int row, int column) {return row*rowSize + column;}
	protected boolean validIndex(int i) {return (i >= 0 && i < icons.length);}
	protected void drawIcon(Graphics2D g, int i) {icons[i].draw(g);}
	
	private int getCellX(BufferedImage img, int column)
	{
		int x = xStart + column * cellWidth;
		switch(cellHorizAlign) {
		case Left:
		case Top:
		case Bottom:
			break;
		case Center:
			x += (cellWidth - img.getWidth()) / 2;
			break;
		case Right:
			x += cellWidth - img.getWidth();
			break;
		}
		return x;
	}
	
	private int getCellY(BufferedImage img, int row)
	{
		int y = yStart + row * rowHeight;
		switch (cellVertAlign) {
		case Top:
		case Left:
		case Right:
			break;
		case Center:
			y += (rowHeight - img.getHeight()) / 2;
			break;
		case Bottom:
			y += rowHeight - img.getHeight();
			break;
		}
		return y;
	}
}
