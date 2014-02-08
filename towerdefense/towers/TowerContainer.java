package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import towerdefense.*;

/**
 * All game towers should be stored in this class
 */
public class TowerContainer
{
	private GamePanel gp;
	private final int drawLimitX;	// do not draw towers past this
	private ArrayList<Tower> currentTowers;
	private Tower newTowerToAdd = null;
	// player clicked on this existing tower
	private Tower gameSelectedTower = null;
	
	// player selected this type of tower from menu
	private TowerType menuSelectedTower = null;
	private BufferedImage menuSelectedTowerSprite = null;
	
	public TowerContainer(GamePanel gp, int drawLimitX)
	{
		this.gp = gp;
		this.drawLimitX = drawLimitX;
		currentTowers = new ArrayList<Tower>();
		add(Tower.newTower(TowerType.TestTowerType, 541, 226));
	}
	
	public void draw(Graphics2D g)
	{
		if (gameSelectedTower != null) {
			gameSelectedTower.drawRangeCircle(g);
		}
		
		for (Tower t : currentTowers) {
			t.draw(g);
		}
		
		if (gp.getMenu().towerIsSelected() &&
				(gp.getMouseX() < drawLimitX)) {
			Tower.drawRangeCircleWithTower(g, menuSelectedTowerSprite, 
					gp.getMouseX(), gp.getMouseY(),
					menuSelectedTower.getRange());
		}
	}
	
	public void setMenuSelectedTower(TowerType tt)
	{
		menuSelectedTower = tt;
		menuSelectedTowerSprite = TowerSprites.getSprite(tt);
	}
	
	public void clearMenuSelectedTower() {menuSelectedTower = null;}
	public boolean menuTowerIsSelected() {return menuSelectedTower != null;}
	
	public void processChanges()
	{
		if (newTowerToAdd != null) {
			currentTowers.add(newTowerToAdd);
			newTowerToAdd = null;
		}
	}
	
	public void add(Tower t) {newTowerToAdd = t;}
}
