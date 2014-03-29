package towerdefense.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import towerdefense.GamePanel;

public class ExitGame extends BasicGameState {
	
	public ExitGame(GamePanel gp, GameState gs)
	{
		super(gp, 0, 0.0);
		switch (gs.getType()) {
		case RunningGame:
			((RunningGame) gs).printStats();
			break;
		case MapSelector:
			break;
		case Default:
			break;
		case LoadingScreen:
			break;
		default:
			break;
		}
		System.exit(0);
	}

	public boolean update(long time) {return false;}
	public void draw(Graphics2D g) {}
	public void processKey(KeyEvent e) {}
	public void mouseClicked(int x, int y) {}
	public void mouseMoved(int x, int y) {}
	public GameState transition() {return null;}
}
