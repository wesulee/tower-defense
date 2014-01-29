import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TowerDefense extends JFrame implements WindowListener
{
	private GamePanel gp;

	public TowerDefense()
	{
		super("Tower Defense");
		Container c = getContentPane();
		gp = new GamePanel(this);
		c.add(gp, "Center");

		addWindowListener(this);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {gp.stopGame();}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	

	public static void main(String[] args)
	{
		TowerDefense td = new TowerDefense();
	}

}
