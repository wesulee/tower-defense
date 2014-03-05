package towerdefense;

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

}
