package towerdefense.gamestates;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import towerdefense.GamePanel;

public class PausedGame extends BasicGameState
{
	private static final int TARGET_FPS = 10;
	private static final float blackAlpha = 0.3f;
	private static final String msg = "Paused";
	private final RunningGame rg;
	private final BufferedImage img;
	private final int drawX;
	private final int drawY;
	private boolean switchState = false;
	
	public PausedGame(GamePanel gp, RunningGame rg)
	{
		super(gp, 500, 5.0);
		rg.notifyPaused();
		gp.setFPS(TARGET_FPS);
		gp.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		this.rg = rg;
		this.img = createBackgroundImage(gp.getImage());
		
		Graphics2D g = (Graphics2D) gp.getGraphics();
		FontMetrics fm = g.getFontMetrics(GamePanel.defaultFont);
		Rectangle2D rect = fm.getStringBounds(msg, g);
		this.drawX = (GamePanel.WIDTH - (int) rect.getWidth()) / 2;
		this.drawY = (GamePanel.HEIGHT - (int) rect.getHeight()) / 2;
		g.dispose();
	}

	public boolean update(final long time)
	{
		if (switchState) {
			rg.notifyUnpaused(time);
		}
		return switchState;
	}

	public void draw(final Graphics2D g)
	{
		final Color oldColor = g.getColor();
		
		g.drawImage(img, 0, 0, null);
		g.setColor(Color.white);
		g.drawString(msg, drawX, drawY);
		
		g.setColor(oldColor);
	}

	public void processKey(final KeyEvent e)
	{
		switch(e.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			gp.setFPS(RunningGame.TARGET_FPS);
			switchState = true;
			break;
		}
	}
	
	public void mouseMoved(final int x, final int y) {}
	public void mouseClicked(final int x, final int y) {}

	private BufferedImage createBackgroundImage(Image img)
	{
		final BufferedImage retImg = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = (Graphics2D) retImg.getGraphics();
		
		g.drawImage(img, 0, 0, null);
		g.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, blackAlpha));
		g.setColor(Color.black);
		g.fillRect(0, 0, retImg.getWidth(), retImg.getHeight());
		
		g.dispose();
		return retImg;
	}
	
	public GameState transition() {return rg;}
	public void printStats() {rg.printStats();}
}
