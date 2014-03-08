package towerdefense;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
		URI uri = null;
		try {
			uri = TowerDefense.class.getResource("Resources/"+path).toURI();
		}
		catch (URISyntaxException e) {
			return "";
		}
		File f = new File(uri);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while (line != null) {
				sb.append(line + '\n');
				line = br.readLine();
			}
		}
		catch (FileNotFoundException e) {
			//System.out.println("Unable to read " + path);
		} catch (IOException e) {
			//System.out.println("Error reading from " + path);
		}
		finally {
			try {
				if (br != null)
					br.close();
			}
			catch (IOException e) {
				//System.out.println("Error closing file " + path);
			}
		}
		
		return sb.toString();
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
