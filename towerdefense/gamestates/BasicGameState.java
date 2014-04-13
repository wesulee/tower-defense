package towerdefense.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import towerdefense.GamePanel;
import towerdefense.util.Utility;

/**
 * Provides basic mouse input for a GameState.
 */
public abstract class BasicGameState implements GameState
{
	protected final GamePanel gp;
	private int mousePressedX;
	private int mousePressedY;
	private long mousePressedTime;
	// maximum delay in ns between mouse press/release to count as click
	private final long mouseReleaseTimeLimit;
	// maximum distance between mouse press/release to count as click
	private final double mouseDistanceLimit;
	
	public BasicGameState(GamePanel gp, int releaseTimeMS, double distance)
	{
		this.gp = gp;
		mouseReleaseTimeLimit = releaseTimeMS * 1000000L;
		mouseDistanceLimit = distance;
	}
	
	public void mousePressed(final MouseEvent e)
	{
		mousePressedTime = System.nanoTime();
		mousePressedX = e.getX();
		mousePressedY = e.getY();
	}
	
	public void mouseReleased(final MouseEvent e)
	{
		long time = System.nanoTime();
		if ((time - mousePressedTime < mouseReleaseTimeLimit) &&
				(Utility.length(
						e.getX() - mousePressedX,
						e.getY() - mousePressedY) <= mouseDistanceLimit)) {
			mouseClicked(mousePressedX, mousePressedY);
		}
	}
	
	public int getMouseX() {return gp.getMouseX();}
	public int getMouseY() {return gp.getMouseY();}
	public void setCurrentCursor(int c) {gp.setCurrentCursor(c);}
	public GameStateType getType()
	{
		return GameStateType.getEnum(getClass().getSimpleName());
	}
	
	public abstract boolean update(final long time);
	public abstract void draw(final Graphics2D g);
	public abstract void processKey(final KeyEvent e);
	public abstract void mouseClicked(final int x, final int y);
	
}
