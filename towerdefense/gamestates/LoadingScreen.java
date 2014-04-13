package towerdefense.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import towerdefense.AssetLoader;
import towerdefense.GamePanel;
import towerdefense.ui.CenteredText;

public abstract class LoadingScreen implements GameState, Runnable
{
	private static final String[] str =
		{"Loading", "Loading .", "Loading . .", "Loading . . ."};
	// number of game updates before switching to next string
	private static final int strUpdateCount = 5;
	private static final Color bgColor = Color.black;
	private static final Color fontColor = Color.white;
	private final CenteredText text;
	
	protected final GamePanel gp;
	protected final AssetLoader assets;
	private final LinkedList<String> files;
	private int strIndex = 0;
	private int updateCount = 0;
	private boolean finishedLoading = false;
	
	public LoadingScreen(GamePanel gp, LinkedList<String> files)
	{
		this.gp = gp;
		gp.setFPS(10);
		
		this.text = new CenteredText(gp, str[0], 0, fontColor);
		this.assets = new AssetLoader(gp);
		this.files= files;
	}

	public abstract GameState transition();

	public boolean update(final long time)
	{
		strIndex = ++updateCount / strUpdateCount % str.length;
		return finishedLoading;
	}

	public void draw(final Graphics2D g)
	{
		final Color oldColor = g.getColor();
		
		g.setColor(bgColor);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		text.draw(g, str[strIndex]);
		
		g.setColor(oldColor);
	}
	
	protected void startLoading()
	{
		Thread t = new Thread(this);
		t.start();
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
	
	// ignore all input
	public void mouseMoved(int x, int y) {}
	public void processKey(KeyEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
