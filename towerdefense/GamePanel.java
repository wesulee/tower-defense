package towerdefense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import towerdefense.creatures.*;
import towerdefense.towers.*;

public class GamePanel extends JPanel implements Runnable, MouseMotionListener
{
	private final static int TARGET_FPS = 60;
	public final static int WIDTH = 900;
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
	private TowerSprites tSprites;
	private ArrayList<Tower> towers = new ArrayList<Tower>();

	public GamePanel(TowerDefense td)
	{
		tdTop = td;
		this.period = ((long)(1000.0 / TARGET_FPS)) * 1000000L;
		
		//setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{ testPress(e.getX(), e.getY()); }
		});
		addMouseMotionListener(this);
		
		// load/setup game data
		map = new Map();
		player = new Player();
		menu = new Menu(WIDTH, HEIGHT, MENU_X, player);
		tSprites = new TowerSprites();
		towers.add(new TestTower(400, 220));
		towers.add(new TestTower(500, 220));
		
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
	
	private void testPress(int x, int y)
	{
		System.out.println("Mouse clicked at ("+x+", "+y+")");
		// not implemented
		if (x > MENU_X) {
			// inside menu
			return;
		}
	}
	
	private void gameUpdate()
	{
		long currentTime = System.nanoTime();
		gameUpdateCount++;
		// not implemented
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
		// Drawing order: background, towers, creatures, projectiles, menu
		
		// draw background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);
		map.draw(dbg);
		
		for (Tower t : towers) {
			t.draw(dbg);
		}
		
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
	
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e)
	{
		if (e.getX() >= MENU_X) {
			menu.notifyMouseMoved(e.getX(), e.getY());
		}
	}

	private void processKey(KeyEvent e)
	{
		int code = e.getKeyCode();
		// not implemented
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