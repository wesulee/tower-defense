package towerdefense.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

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
	
	private final GamePanel gp;
	private final AssetLoader assets;
	private final int drawStringX;
	private final int drawStringY;
	private final LinkedList<String> files;
	private int strIndex = 0;
	private int updateCount = 0;
	private boolean finishedLoading = false;
	
	public LoadingScreen(GamePanel gp, LinkedList<String> files)
	{
		this.gp = gp;
		gp.setFPS(10);
		Graphics2D g = (Graphics2D) gp.getGraphics();
		FontMetrics fm = g.getFontMetrics(GamePanel.defaultFont);
		Rectangle2D rect = fm.getStringBounds(str[0], g);
		drawStringX = (GamePanel.WIDTH - (int)rect.getWidth()) / 2;
		drawStringY = (GamePanel.HEIGHT - (int)rect.getHeight()) / 2;
		g.dispose();
		
		assets = new AssetLoader();
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
		
		for (String path : files)
			assets.get(path, true);
		
		long diff = System.currentTimeMillis() - time;
		if (diff < duration) {
			try {
				Thread.sleep(duration - diff);
			} catch (InterruptedException e) {}
		}
		
		finishedLoading = true;
	}
	
	public AssetLoader getAssets() {return assets;}
	public GameStateType getType() {return GameStateType.LoadingScreen;}
	protected GamePanel getGamePanel() {return gp;}
	
	// ignore all input
	public void mouseMoved(int x, int y) {}
	public void processKey(KeyEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
