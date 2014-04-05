package towerdefense;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class TowerDefense extends JFrame
{
	private GamePanel gp;
	public static final boolean DEBUG = false;

	public TowerDefense()
	{
		super("Tower Defense");
		Container c = getContentPane();
		gp = new GamePanel(this);
		c.add(gp);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				gp.stopGame();
			}
		});
		
		setIgnoreRepaint(true);
		setResizable(false);
		setVisible(true);
		pack();
	}

	public static void main(String[] args)
	{
		new TowerDefense();
	}

}
