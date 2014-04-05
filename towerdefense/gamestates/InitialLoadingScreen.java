package towerdefense.gamestates;

import java.util.LinkedList;

import towerdefense.GamePanel;
import towerdefense.maps.MapType;

public class InitialLoadingScreen extends LoadingScreen
{	
	public InitialLoadingScreen(GamePanel gp)
	{
		super(gp, getPaths());
	}
	
	public static LinkedList<String> getPaths()
	{
		LinkedList<String> paths = new LinkedList<String>();
		
		for (MapType mt : MapType.values())
			paths.add(mt.getPath());
		
		paths.add("Projectiles/fireball_small.png");
		paths.add("Projectiles/fireball_large.png");
		
		return paths;
	}
	
	public GameState transition()
	{
		return new MapSelector(getGamePanel(), this);
	}
}
