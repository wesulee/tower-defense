package towerdefense.gamestates;

import towerdefense.GamePanel;
import towerdefense.maps.MapType;

public class InitialLoadingScreen extends LoadingScreen
{
	private final GamePanel gp;
	
	public InitialLoadingScreen(GamePanel gp)
	{
		super(gp, getPaths());
		this.gp = gp;
	}
	
	public static String[] getPaths()
	{
		int size = 0;
		size += MapType.values().length;
		int i = 0;
		String[] paths = new String[size];
		for (MapType mt : MapType.values())
			paths[i++] = mt.getPath();
		
		return paths;
	}
	
	public GameState transition()
	{
		return new MapSelector(gp, this);
	}
}
