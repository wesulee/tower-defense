package towerdefense.maps;

import java.awt.Rectangle;

import towerdefense.util.Utility;

public class MapParser
{
	private int pathRectCount;
	private Rectangle[] creaturePath;
	private Rectangle spawnRect;
	private int pathPointCount;
	private int[] pathX;
	private int[] pathY;
	private int[] pathDistance;
	
	// invalid until completely parsed without errors
	private boolean valid = false;
	private String error;
	
	public MapParser(MapType mt)
	{
		String path = mt.getDataPath();
		String str = Utility.readStrFromFile(path);
		if (str == null) {
			error = "data file could not be read";
			return;
		}
		if (str.isEmpty()) {
			error = "empty data";
			return;
		}
		String[] lines = str.split("\\r?\\n");
		
		// parse creaturePath rectangles
		String line = lines[0];
		try {
			pathRectCount = Integer.parseInt(line);
		}
		catch (NumberFormatException e) {
			error = "could not parse pathRectCount";
			return;
		}
		if (!validateLines(lines, 1, pathRectCount, 4)) {
			error = "invalid pathRect";
			return;
		}
		creaturePath = new Rectangle[pathRectCount];
		fillRectArray(lines, 1, pathRectCount, creaturePath);
		int lineIndex = nextIntegerIndex(lines, 1 + pathRectCount);
		if (lineIndex == -1) {
			error = "incomplete data (missing spawnRect)";
			return;
		}
		
		// parse spawnRect
		if (!validateLines(lines, lineIndex, 1, 4)) {
			error = "invalid spawnRect";
			return;
		}
		spawnRect = strToRect(lines[lineIndex]);
		lineIndex = nextIntegerIndex(lines, lineIndex + 1);
		if (lineIndex == -1) {
			error = "incomplete data (missing path points)";
			return;
		}
		
		// parse creature path points
		try {
			pathPointCount = Integer.parseInt(lines[lineIndex]);
		}
		catch (NumberFormatException e) {
			error = "could not parse pathPointCount";
			return;
		}
		if (!validateLines(lines, ++lineIndex, pathPointCount, 3)) {
			error = "invalid path points";
			return;
		}
		pathX = new int[pathPointCount];
		pathY = new int[pathPointCount];
		pathDistance = new int[pathPointCount];
		fillPathPoints(lines, lineIndex, pathPointCount,
				pathX, pathY, pathDistance);
		valid = true;
	}
	
	public boolean isValid() {return valid;}
	public String getError() {return error;}
	public Rectangle[] getCreaturePath() {return creaturePath;}
	public Rectangle getSpawnRect() {return spawnRect;}
	public int[] getPathX() {return pathX;}
	public int[] getPathY() {return pathY;}
	public int[] getPathDistance() {return pathDistance;}
	
	// validates lines[index] to line[index + lineCount]
	// checks if each line contains integerCount integers
	private boolean validateLines(String[] lines, int index, int lineCount,
			int integerCount)
	{
		int[] integers;
		for (int i = index; i < index + lineCount; i++) {
			try {
				integers = Utility.getInts(lines[i]);
			}
			catch (NumberFormatException e) {
				return false;
			}
			if (integers.length != integerCount)
				return false;
		}
		return true;
	}
	
	// returns index of line where first integer occurs after index
	private int nextIntegerIndex(String[] lines, int index)
	{
		String line;
		for (int i = index; i < lines.length; i++) {
			line = lines[i];
			for (int j = 0; j < line.length(); j++) {
				if (Character.isDigit(line.charAt(j)))
					return i;
			}
		}
		return -1;
	}
	
	private void fillRectArray(String[] lines, int index, int lineCount,
			Rectangle[] rectArray)
	{
		for (int i = index; i < index + lineCount; i++) {
			rectArray[i - index] = strToRect(lines[i]);
		}
	}
	
	private Rectangle strToRect(String str)
	{
		int[] integers = Utility.getInts(str);
		return new Rectangle(
				integers[0], integers[1], integers[2], integers[3]);
	}
	
	private void fillPathPoints(String[] lines, int index, int lineCount,
			int[] x, int[] y, int[] d)
	{
		int[] integers;
		for (int i = index; i < index + lineCount; i++) {
			integers = Utility.getInts(lines[i]);
			x[i - index] = integers[0];
			y[i - index] = integers[1];
			d[i - index] = integers[2];
		}
	}
}
