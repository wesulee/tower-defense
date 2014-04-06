package towerdefense.gamestates;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import towerdefense.GamePanel;
import towerdefense.Menu;
import towerdefense.Player;
import towerdefense.AssetLoader;
import towerdefense.TowerDefense;
import towerdefense.WaveController;
import towerdefense.creatures.CreatureContainer;
import towerdefense.maps.GameMap;
import towerdefense.maps.MapType;
import towerdefense.towers.Tower;
import towerdefense.towers.TowerContainer;
import towerdefense.towers.TowerType;

public class RunningGame extends BasicGameState
{
	private final GamePanel gp;
	private final long startTime;
	private long updateCount = 0;
	private final int TARGET_FPS = 60;
	// MENU_X determines where menu is drawn
	public static final int MENU_X = 800;
	private final AssetLoader assets;
	private final GameMap map;
	private final Player player;
	private final Menu menu;
	private final TowerContainer towers;
	private final CreatureContainer creatures;
	private final WaveController wc;
	// already existing tower selected
	private Tower selectedTower = null;
	
	public RunningGame(GamePanel gp, MapType mt, AssetLoader assets)
	{
		super(gp, 500, 5.0);
		this.gp = gp;
		gp.setFPS(TARGET_FPS);
		gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		
		this.assets = assets;
		assets.unloadMapsExcept(mt);
		map = new GameMap(this, mt);
		player = new Player();
		towers = new TowerContainer(this, MENU_X);
		creatures = new CreatureContainer(this);
		wc = new WaveController(this);
		menu = new Menu(this, player);
		player.setMenu(menu);
		menu.notifyGoldChange();
		
		startTime = System.nanoTime();
	}
	
	public GamePanel getGamePanel() {return gp;}
	public Player getPlayer() {return player;}
	public Menu getMenu() {return menu;}
	public GameMap getMap() {return map;}
	public TowerContainer getTowerContainer() {return towers;}
	public CreatureContainer getCreatureContainer() {return creatures;}
	public WaveController getWaveController() {return wc;}
	public AssetLoader getAssetLoader() {return assets;}

	public boolean update(final long time)
	{
		updateCount++;
		towers.update(time, creatures);
		creatures.update();
		wc.update(time);
		return false;
	}

	public void draw(final Graphics2D g)
	{
		// Drawing order: background, towers, creatures, projectiles, menu
		map.draw(g);
		
		towers.draw(g);
		if (selectedTower != null)
			selectedTower.drawRangeCircle(g);
		
		creatures.draw(g);
		
		menu.draw(g);
	}
	
	public void mouseClicked(final int x, final int y)
	{
		if (TowerDefense.DEBUG)
			System.out.println("Mouse clicked at (" + x + ", " + y + ")");
		if (x >= MENU_X) {
			menu.mouseClicked(x, y);
			return;
		}
		
		// check if need to create new tower
		if (menu.towerIsSelected()) {
			TowerType tt = menu.getSelectedTower();
			if (towers.canAddTower(tt, x, y)) {
				menu.clearSelectedTower();
				towers.add(tt, x, y);
				player.decreaseGold(tt.getCost());
			}
			else {
				System.out.println("Cannot build tower there.");
			}
		}
		else {
			selectedTower = towers.getTowerAt(x, y);
		}
	}
	
	public void mouseMoved(final int x, final int y)
	{
		if (x >= MENU_X) {
			menu.mouseMoved(x, y);
		}
		else {
			//gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		}
	}
	
	public void processKey(final KeyEvent e)
	{
		int code = (int)e.getKeyChar();
		// Escape key
		if (code == 27) {
			menu.clearSelectedTower();
			selectedTower = null;
			return;
		}
	}
	
	public GameState transition()
	{
		return new ExitGame(gp, this);
	}
		
	public void printStats()
	{
		long elapseTime = (System.nanoTime()-startTime) / 1000000L; // ms
		double elapse = elapseTime / 1000.0;
		double targetFPS = 1000.0 / gp.getPeriod() * 1000000L;
		System.out.println("Target FPS: "+targetFPS);
		System.out.println("Period: " + (gp.getPeriod() / 1000) / 1000.0 + " ms");
		System.out.println("Average FPS: " + updateCount / elapse);
		System.out.println("gameUpdateCount: " + updateCount);
		System.out.println("Elapse time: " + elapse + " s");
	}
}
