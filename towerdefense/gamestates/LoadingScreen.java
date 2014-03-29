package towerdefense.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import towerdefense.AssetLoader;
import towerdefense.GamePanel;

public abstract class LoadingScreen implements GameState, Runnable
{
	private static final String[] str =
		{"Loading", "Loading .", "Loading . .", "Loading . . ."};
	// number of game updates before switching to next string
	private static final int strUpdateCount = 5;
	private static final Color bgColor = Color.black;
	private static final Color fontColor = Color.white;
	
	private final HashMap<String, BufferedImage> assets;
	private final int drawStringX;
	private final int drawStringY;
	private final String[] files;
	private int strIndex = 0;
	private int updateCount = 0;
	private boolean finishedLoading = false;
	
	public LoadingScreen(GamePanel gp, String[] files)
	{
		gp.setFPS(10);
		Graphics2D g = (Graphics2D) gp.getGraphics();
		FontMetrics fm = g.getFontMetrics(GamePanel.defaultFont);
		Rectangle2D rect = fm.getStringBounds(str[0], g);
		drawStringX = (GamePanel.WIDTH - (int)rect.getWidth()) / 2;
		drawStringY = (GamePanel.HEIGHT - (int)rect.getHeight()) / 2;
		g.dispose();
		
		assets = new HashMap<String, BufferedImage>();
		this.files= files;
		
		Thread t = new Thread(this);
		t.start();
	}

	public abstract GameState transition();

	public boolean update(long time)
	{
		strIndex = ++updateCount / strUpdateCount % str.length;
		return finishedLoading;
	}

	public void draw(Graphics2D g)
	{
		Color oldColor = g.getColor();
		
		g.setColor(bgColor);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(fontColor);
		g.drawString(str[strIndex], drawStringX, drawStringY);
		
		g.setColor(oldColor);
	}
	
	public void run()
	{
		// display loading screen for at least 2 seconds
		long duration = 2000;
		long time = System.currentTimeMillis();
		
		BufferedImage tmp;
		for (String path : files) {
			tmp = AssetLoader.loadResource(path);
			assets.put(path, tmp);
		}
		
		long diff = System.currentTimeMillis() - time;
		if (diff < duration) {
			try {
				Thread.sleep(duration - diff);
			} catch (InterruptedException e) {}
		}
		
		finishedLoading = true;
	}
	
	public HashMap<String, BufferedImage> getAssets() {return assets;}
	
	// ignore all input
	public void mouseMoved(int x, int y) {}
	public void processKey(KeyEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public GameStateType getType() {return GameStateType.LoadingScreen;}
}
