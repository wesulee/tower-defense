import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable
{
	private final static int TARGET_FPS = 60;
	private static int WIDTH = 900;
	private static int HEIGHT = 600;
	// MENU_X determines where menu is drawn
	private static int MENU_X = 800;
	
	private Thread animator;
	private boolean running = false;
	private boolean isPaused = false;
	private long period; // redraw delay, nanoseconds
	
	protected long gameStartTime;
	protected long gameUpdateCount = 0L;
	
	private TowerDefense tdTop;
	
	private Graphics2D dbg;
	private Image dbImage = null;


	
	private BufferedImage background;
	private Player player;
	private Menu menu;

	public GamePanel(TowerDefense td)
	{
		tdTop = td;
		this.period = ((long)(1000.0 / TARGET_FPS)) * 1000000L;
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{ testPress(e.getX(), e.getY()); }
		});
		
		try {
			background = ImageIO.read(getClass().getResource("/Resources/test_map.png"));
		} 
		catch (IOException e) {
			System.out.println("Unable to load test_map.png");
		}
		player = new Player();
		menu = new Menu(WIDTH, HEIGHT, MENU_X, player);
		
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
		// not implemented
	}
	
	private void gameUpdate()
	{
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
		dbg.drawImage(background, 0, 0, null);
		
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

	private void processKey(KeyEvent e)
	{
		int code = e.getKeyCode();
	}
	
	private void printStats()
	{
		long elapseTime = (System.nanoTime()-gameStartTime) / 1000000L; // ms
		double elapse = elapseTime / 1000.0;
		System.out.println("Average FPS: " + gameUpdateCount / elapse);
		System.out.println("gameUpdateCount: " + gameUpdateCount);
		System.out.println("Elapse time: " + elapse + " s");
	}
}