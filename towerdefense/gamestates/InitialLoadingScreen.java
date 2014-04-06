package towerdefense.gamestates;

import java.util.LinkedList;

import towerdefense.GamePanel;
import towerdefense.maps.MapType;
import towerdefense.projectiles.Fireball;
import towerdefense.projectiles.Frost;

public class InitialLoadingScreen extends LoadingScreen
{	
	public InitialLoadingScreen(GamePanel gp)
	{
		super(gp, getPaths());
		if (!assets.validateAssets()) {
			gp.errorDialog("Asset Validation Error",
					assets.getValidationErrorMsg());
		}
		startLoading();
	}
	
	// assets to be loaded
	public static LinkedList<String> getPaths()
	{
		LinkedList<String> paths = new LinkedList<String>();
		
		for (MapType mt : MapType.values())
			paths.add(mt.getPath());
		
		paths.add(Fireball.path1);
		paths.add(Fireball.path2);
		paths.add(Frost.path);
		
		return paths;
	}
	
	public GameState transition() {return new MapSelector(gp, this);}
}
