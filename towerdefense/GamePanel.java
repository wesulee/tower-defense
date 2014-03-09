package towerdefense;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import towerdefense.gamestates.ExitGame;
import towerdefense.gamestates.GameState;
import towerdefense.gamestates.MapSelector;

public class GamePanel extends JPanel implements Runnable
{
	public static final int TARGET_FPS = 60;
	public static final int WIDTH = 950;
	public final static int HEIGHT = 600;
	
	private int mouseX = 0;
	private int mouseY = 0;
	private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private int currentCursor = Cursor.DEFAULT_CURSOR;
	
	private Thread animator;
	private boolean running = false;
	//private boolean isPaused = false;
	// redraw delay, nanoseconds
	public static long period;
		
	private TowerDefense tdTop;
	private Graphics2D dbg;
	private Image dbImage = null;
	private GameState gs;
	
	public GamePanel(TowerDefense td)
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				gs.mousePressed(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				gs.mouseReleased(e);
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				_mouseMoved(e.getX(), e.getY());
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				gs.processKey(e);
			}
		});
		
		tdTop = td;
		//gs = new RunningGame(this, "test_map.png");
		gs = new MapSelector(this);
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
	
	public void stopGame() {gs = new ExitGame(this, gs);}
	public void pauseGame() {/* not implemented */}
	public void resumeGame() {/* not implemented */}
	public long getRedrawDelay() {return period;}
	public int getMouseX() {return mouseX;}
	public int getMouseY() {return mouseY;}
	
	
	public void run()
	{
		long beforeTime, afterTime, timeDiff, sleepTime;
		
		beforeTime = System.nanoTime();
		
		running = true;
		while(running) {
			gameUpdate();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			if (period - timeDiff < 0) {
				sleepTime = 5000000L;	// 5 ms
			}
			else {
				sleepTime = period - timeDiff;
			}
			try {
				Thread.sleep(sleepTime / 1000000L);	// ms
			}
			catch(InterruptedException e) {}
			
			
			beforeTime = System.nanoTime();
		}
	}
	
	private void gameUpdate()
	{
		if(gs.update(System.nanoTime()))
			gs = gs.transition();
	}
	
	private void gameRender()
	{
		if (dbImage == null) {
			dbImage = createImage(WIDTH, HEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			}
			else {
				dbg = (Graphics2D) dbImage.getGraphics();
				dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
			}
		}
		
		// draw background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);
		
		gs.draw(dbg);
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
		gs.mouseMoved(x, y);
	}

	private void processKey(KeyEvent e)
	{
		gs.processKey(e);
	}
	
	public void setFPS(int fps)
	{
		period = (long)(1000.0 / fps * 1000000.0);
	}
	
	public long getPeriod() {return period;}
	public int getCurrentCursor() {return currentCursor;}
	
	public void setCurrentCursor(int cursor)
	{
		switch(cursor) {
		case Cursor.HAND_CURSOR:
			setCursor(handCursor);
			break;
		case Cursor.DEFAULT_CURSOR:
			setCursor(defaultCursor);
			break;
		default:
			setCursor(defaultCursor);
		}
		currentCursor = cursor;
	}
}