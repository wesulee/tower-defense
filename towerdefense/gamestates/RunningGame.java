package towerdefense.gamestates;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import towerdefense.GamePanel;
import towerdefense.TowerDefense;
import towerdefense.components.AssetLoader;
import towerdefense.components.Player;
import towerdefense.components.WaveController;
import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureContainer;
import towerdefense.maps.GameMap;
import towerdefense.maps.MapType;
import towerdefense.towers.Tower;
import towerdefense.towers.TowerContainer;
import towerdefense.towers.TowerType;
import towerdefense.ui.Menu;

public class RunningGame extends BasicGameState
{
	public static final int TARGET_FPS = 60;
	// MENU_X determines where menu is drawn
	public static final int MENU_X = 800;
	private final GamePanel gp;
	private long startTime;
	private long elapseTime = 0;	// excludes pause time
	private long updateCount = 0;
	private boolean switchState = false;	// change game state?
	private boolean pauseActivated = false;
	private boolean gameLost = false;
	
	// game components
	private final AssetLoader assets;
	private final GameMap map;
	private final Player player;
	private final Menu menu;
	private final TowerContainer towers;
	private final CreatureContainer creatures;
	private final WaveController wc;
	// already existing tower selected
	private Tower selectedTower = null;
	
	private Creature selectedCreature = null;	// debug variable
	
	public RunningGame(GamePanel gp, MapType mt, AssetLoader assets)
	{
		super(gp, 500, 5.0);
		this.gp = gp;
		gp.setFPS(TARGET_FPS);
		gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		
		this.assets = assets;
		assets.unloadMapsExcept(mt);
		map = new GameMap(this, mt);
		player = new Player(this);
		towers = new TowerContainer(this, MENU_X);
		creatures = new CreatureContainer(this);
		wc = new WaveController(this);
		menu = new Menu(this, player);
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
		return switchState;
	}

	public void draw(final Graphics2D g)
	{
		// Drawing order: background, towers, creatures, projectiles, menu
		map.draw(g);
		
		towers.drawTowers(g);
		if (selectedTower != null)
			selectedTower.drawRangeCircle(g);
		
		creatures.draw(g);
		
		towers.drawProjectiles(g);
		
		if (TowerDefense.DEBUG && (selectedCreature != null))
			selectedCreature.drawDebug(g);
		
		menu.draw(g);
	}
	
	public void mouseClicked(final int x, final int y)
	{
		if (TowerDefense.DEBUG) {
			System.out.println("Mouse clicked at (" + x + ", " + y + ")");
			selectedCreature = creatures.getCreatureAt(x, y);
		}
		
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
			gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		}
	}
	
	public void processKey(final KeyEvent e)
	{
		switch(e.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			if (menu.towerIsSelected()) {
				menu.clearSelectedTower();
			}
			else if (selectedTower != null) {
				selectedTower = null;
			}
			else {	// activate pause menu
				pauseActivated = true;
				switchState = true;
			}
			break;
		}
	}
	
	public GameState transition()
	{
		if (pauseActivated) {
			pauseActivated = false;
			switchState = false;
			return new PausedGame(gp, this);
		}
		else if (gameLost) {
			switchState = false;
			menu.clearSelectedTower();
			return new GameLost(this);
		}
		else {
			return new ExitGame(gp, this);
		}
	}
	
	public void notifyGameLost()
	{
		gameLost = true;
		switchState = true;
	}
	
	public void notifyPaused()
	{
		elapseTime += System.nanoTime() - startTime;
	}
	
	public void notifyUnpaused(final long time)
	{
		startTime = time;
	}
	
	public void printStats()
	{
		double elapse = (System.nanoTime()-startTime + elapseTime)
				/ 1000000L / 1000.0;
		double targetFPS = 1000.0 / gp.getPeriod() * 1000000L;
		System.out.println("Target FPS: "+targetFPS);
		System.out.println("Period: " + (gp.getPeriod() / 1000)
				/ 1000.0 + " ms");
		System.out.println("Average FPS: " + updateCount / elapse);
		System.out.println("gameUpdateCount: " + updateCount);
		System.out.println("Elapse time: " + elapse + " s");
	}
}
