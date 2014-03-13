package towerdefense.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import towerdefense.Utility;

public class Icon
{
	private final BufferedImage img;
	private final int x1;
	private final int y1;
	private final int x2;
	private final int y2;
	
	public Icon(BufferedImage img, int pos_x, int pos_y)
	{
		this.img = img;
		this.x1 = pos_x;
		this.y1 = pos_y;
		this.x2 = x1 + img.getWidth();
		this.y2 = y1 + img.getHeight();
	}
	
	public BufferedImage getImage() {return Utility.createCopy(img);}
	public int getX() {return x1;}
	public int getY() {return y1;}
	public void draw(Graphics2D g) {g.drawImage(img, x1, y1, null);}
	public boolean contains(int x, int y)
	{
		return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2));
	}
	public Rectangle getRect()
	{
		return new Rectangle(x1, y1, img.getWidth(), img.getHeight());
	}
}
