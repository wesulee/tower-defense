package towerdefense.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameState
{
	boolean update(long time);
	void draw(Graphics2D g);
	void mousePressed(MouseEvent e);
	void mouseReleased(MouseEvent e);
	void mouseMoved(int x, int y);
	void processKey(KeyEvent e);
	GameState transition();
	GameStateType getType();
}
