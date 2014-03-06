package towerdefense.GameStates;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import towerdefense.GameMap;
import towerdefense.GamePanel;
import towerdefense.Menu;
import towerdefense.Player;
import towerdefense.SpriteContainer;
import towerdefense.WaveController;
import towerdefense.creatures.CreatureContainer;
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
	private final SpriteContainer sprites = new SpriteContainer();
	private final GameMap map;
	private final Player player;
	private final Menu menu;
	private final TowerContainer towers;
	private final CreatureContainer creatures;
	private final WaveController wc;
	private Tower selectedTower = null;
	
	public RunningGame(GamePanel gp, String mapName)
	{
		super(gp, 500, 5.0);
		this.gp = gp;
		gp.setFPS(TARGET_FPS);
		gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		
		map = new GameMap(mapName);
		player = new Player();
		towers = new TowerContainer(this, MENU_X);
		creatures = new CreatureContainer(this);
		wc = new WaveController(this);
		menu = new Menu(GamePanel.WIDTH, GamePanel.HEIGHT, MENU_X, this, player);
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

	public void update(long time)
	{
		updateCount++;
		towers.update(time, creatures);
		creatures.update();
		wc.update(time);
	}

	public void draw(Graphics2D g)
	{
		// Drawing order: background, towers, creatures, projectiles, menu
		map.draw(g);
		
		towers.draw(g);
		creatures.draw(g);
		
		menu.draw(g);
	}
	
	public void mouseClicked(int x, int y)
	{
		System.out.println("Mouse clicked at ("+x+", "+y+")");
		if (x >= MENU_X) {
			menu.notifyMouseClicked(x, y);
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
	}
	
	public void mouseMoved(int x, int y)
	{
		if (x >= MENU_X) {
			menu.notifyMouseMoved(x, y);
		}
		else {
			gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		}
	}
	
	public void processKey(KeyEvent e)
	{
		int code = (int)e.getKeyChar();
		// Escape key
		if (code == 27) {
			menu.clearSelectedTower();
			selectedTower = null;
			return;
		}
	}
		
	public void cleanUp()
	{
		printStats();
	}
	
	private void printStats()
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
