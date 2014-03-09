package towerdefense;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import java.io.InputStream;

public final class Utility
{
	private Utility() {}
	
	public static double length(int a, int b)
	{
		return Math.sqrt(a*a + b*b);
	}
	
	public static double length(double a, double b)
	{
		return Math.sqrt(a*a + b*b);
	}
	
	// returns fname with file extension
	public static String excludeExt(String fname)
	{
		return fname.substring(0, fname.lastIndexOf("."));
	}
	
	public static String readStrFromFile(String path)
	{
		path = "Resources/" + path;
		InputStream is = TowerDefense.class.getResourceAsStream(path);
		if (is == null) {
			return null;
		}
		java.util.Scanner s = new java.util.Scanner(is);
		java.util.Scanner s2 = s.useDelimiter("\\A");
		String output = s2.hasNext() ? s2.next() : "";
		s.close();
		s2.close();
		return output;
	}
	
	public static int[] getInts(String str)
	{
		String[] split = str.split("\\s+");
		int[] ints = new int[split.length];
		
		for (int i = 0; i < split.length; i++)
			ints[i] = Integer.parseInt(split[i]);
		
		return ints;
	}
	
	// return a clone of img
	public static BufferedImage createCopy(BufferedImage img)
	{
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	// return rescaled image that fits inside a rectangle of x by y
	public static BufferedImage scaledResize(BufferedImage img, int x, int y)
	{
		double scaleDiv = minimum((double)img.getWidth() / (double)x,
				(double)img.getHeight() / (double)y);
		BufferedImage resized = new BufferedImage(
				(int)(img.getWidth() / scaleDiv),
				(int)(img.getHeight() / scaleDiv),
				img.getType()
		);
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, resized.getWidth(), resized.getHeight(), null);
		g.dispose();
		return resized;
	}
	
	public static double minimum(double a, double b)
	{
		return a > b ? b : a;
	}
	
	public static int minimum(int a, int b)
	{
		return a > b ? b : a;
	}
}
