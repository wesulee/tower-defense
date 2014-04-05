package towerdefense.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class DummyGameState implements GameState
{
	public boolean update(long time) {return false;}
	public void draw(Graphics2D g) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(int x, int y) {}
	public void processKey(KeyEvent e) {}
	public GameState transition() {return null;}
	public GameStateType getType() {return null;}
}
