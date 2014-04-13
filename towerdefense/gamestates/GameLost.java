package towerdefense.gamestates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import towerdefense.GamePanel;
import towerdefense.animation.ShapeFadeIn;
import towerdefense.ui.CenteredText;

public class GameLost extends BasicGameState
{
	private RunningGame rg;
	private boolean updateRG = true;
	private boolean imgUpdated = false;	// has static image been saved?
	private boolean switchState = false;
	private final ShapeFadeIn fade;
	private final CenteredText text;
	private final BufferedImage img;
	
	public GameLost(RunningGame rg)
	{
		super(rg.getGamePanel(), 500, 5.0);
		this.rg = rg;
		gp.setFPS(24);
		Rectangle fadeRect = new Rectangle(0, 0,
				GamePanel.WIDTH,GamePanel.HEIGHT);
		this.fade = new ShapeFadeIn(fadeRect, 2000, 0.5f);
		this.text = new CenteredText(gp, "Game Over", 0, Color.red);
		this.img = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT,
				BufferedImage.TYPE_INT_RGB);
	}

	public boolean update(long time)
	{
		if (updateRG) {
			rg.update(time);
			if (rg.getCreatureContainer().getCreatureList().isEmpty())
				updateRG = false;
		}
		fade.update();
		return switchState;
	}

	public void draw(final Graphics2D g)
	{
		if (imgUpdated)
			g.drawImage(img, 0, 0, null);
		else if (updateRG)
			rg.draw(g);
		else {
			Graphics2D g2 = (Graphics2D) img.getGraphics();
			rg.draw(g2);
			g2.dispose();
			g.drawImage(img, 0, 0, null);
			imgUpdated = true;
		}
		g.setColor(Color.black);
		fade.draw(g);
		text.draw(g);
	}
	
	public GameState transition() {return new ExitGame(gp, this);}

	public void mouseMoved(int x, int y){}
	public void mouseClicked(int x, int y) {}
	public void processKey(KeyEvent e) {}
}
