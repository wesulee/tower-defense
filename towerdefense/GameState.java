package towerdefense;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameState
{
	void update(long time);
	void draw(Graphics2D g);
	void mousePressed(MouseEvent e);
	void mouseReleased(MouseEvent e);
	void mouseMoved(int x, int y);
	void processKey(KeyEvent e);
	void cleanUp();		// called before game state transition
}
