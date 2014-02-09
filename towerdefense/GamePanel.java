package towerdefense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import towerdefense.creatures.*;
import towerdefense.towers.*;

public class GamePanel extends JPanel implements Runnable
{
	private final static int TARGET_FPS = 60;
	public final static int WIDTH = 950;
	public final static int HEIGHT = 600;
	// MENU_X determines where menu is drawn
	private static int MENU_X = 800;
	
	private Thread animator;
	private boolean running = false;
	//private boolean isPaused = false;
	private long period; // redraw delay, nanoseconds
	
	protected long gameStartTime;
	protected long gameUpdateCount = 0L;
	
	private TowerDefense tdTop;
	
	private Graphics2D dbg;
	private Image dbImage = null;
	
	private Map map;
	private Player player;
	private Menu menu;
	private TowerContainer towers;
	private CreatureContainer creatures;
	private int mouseX = 0;
	private int mouseY = 0;

	public GamePanel(TowerDefense td)
	{
		tdTop = td;
		this.period = ((long)(1000.0 / TARGET_FPS)) * 1000000L;
		
		//setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				_mouseClicked(e.getX(), e.getY());
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				_mouseMoved(e.getX(), e.getY());
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				processKey(e);
			}
		});
		
		// load/setup game data
		new TowerSprites();
		map = new Map(this);
		player = new Player();
		towers = new TowerContainer(this, MENU_X);
		creatures = new CreatureContainer(this);
		menu = new Menu(WIDTH, HEIGHT, MENU_X, this, player);
		player.setMenu(menu);
		menu.notifyGoldChange();
		
	}
	
	public void addNotify()
	{
		super.addNotify();
		startGame();
	}
	
	private void startGame()
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void stopGame() {running = false;}
	public void pauseGame() {/* not implemented */}
	public void resumeGame() {/* not implemented */}
	public int getMouseX() {return mouseX;}
	public int getMouseY() {return mouseY;}
	public Player getPlayer() {return player;}
	public Menu getMenu() {return menu;}
	public Map getMap() {return map;}
	public TowerContainer getTowerContainer() {return towers;}
	public CreatureContainer getCreatureContainer() {return creatures;}
	
	public void run()
	{
		long beforeTime, afterTime, timeDiff, sleepTime;
		
		gameStartTime = System.nanoTime();
		beforeTime = System.nanoTime();
		
		running = true;
		while(running) {
			gameUpdate();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime - period;
			if (timeDiff < 0) {
				sleepTime = 5000000L;	// 5 ms
			}
			else {
				sleepTime = timeDiff;
			}
			try {
				Thread.sleep(sleepTime / 1000000L);	// ms
			}
			catch(InterruptedException e) {}
			
			
			beforeTime = System.nanoTime();
		}
		printStats();
		System.exit(0);
	}
	
	private void gameUpdate()
	{
		long currentTime = System.nanoTime();
		gameUpdateCount++;
		
		towers.update();
	}
	
	private void gameRender()
	{
		if (dbImage == null) {
			dbImage = createImage(WIDTH, HEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			}
			else
				dbg = (Graphics2D) dbImage.getGraphics();
		}
		dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Drawing order: background, towers, creatures, projectiles, menu
		
		// draw background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);
		map.draw(dbg);
		
		towers.draw(dbg);
		
		menu.draw(dbg);
	}
	
	private void paintScreen()
	{
		Graphics g;
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			g.dispose();
		}
		catch (Exception e) {
			System.out.println("Graphics context error: + e");
		}
	}
	
	public void _mouseMoved(int x, int y)
	{
		mouseX = x;
		mouseY = y;
		
		if (x >= MENU_X) {
			menu.notifyMouseMoved(x, y);
			return;
		}
	}
	
	public void _mouseClicked(int x, int y)
	{
		System.out.println("Mouse clicked at ("+x+", "+y+")");
		if (x >= MENU_X) {
			menu.notifyMouseClicked(x, y);
			return;
		}
		
		// check if need to create new tower
		if (menu.towerIsSelected()) {
			TowerType tt = menu.getSelectedTower();
			int size = tt.getSize();
			Rectangle r = new Rectangle(x-size, y-size, size*2, size*2);
			if (map.spotAvailableForTower(r) &&
					!towers.intersectsTowers(r)) {
				menu.clearSelectedTower();
				Tower newTower = Tower.newTower(tt, x, y);
				if (newTower == null) return;
				towers.add(newTower);
				player.decreaseGold(newTower.getCost());
				menu.notifyGoldChange();
			}
			else {
				System.out.println("Cannot build tower there.");
			}
		}
	}

	private void processKey(KeyEvent e)
	{
		int code = (int)e.getKeyChar();
		// Escape key
		if (code == 27) {
			menu.clearSelectedTower();
			return;
		}
	}
	
	private void printStats()
	{
		long elapseTime = (System.nanoTime()-gameStartTime) / 1000000L; // ms
		double elapse = elapseTime / 1000.0;
		double targetFPS = 1000.0 / period * 1000000L;
		System.out.println("Target FPS: "+targetFPS);
		System.out.println("Period: " + (period / 1000) / 1000.0 + " ms");
		System.out.println("Average FPS: " + gameUpdateCount / elapse);
		System.out.println("gameUpdateCount: " + gameUpdateCount);
		System.out.println("Elapse time: " + elapse + " s");
	}
}