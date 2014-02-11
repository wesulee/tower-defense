package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	
	public void update(long time)
	{
		processChanges();
		
		// not implemented
		/*for (Tower t : currentTowers) {
			if (t.canAttack(time)) {
				t.attack();
			}
		}*/
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
		menuSelectedTowerSprite = SpriteContainer.getSprite(tt);
	}
	
	public void clearMenuSelectedTower() {menuSelectedTower = null;}
	public boolean menuTowerIsSelected() {return menuSelectedTower != null;}
	// should call update between adding multiple towers
	public void add(Tower t) {newTowerToAdd = t;}
	
	private void processChanges()
	{
		if (newTowerToAdd != null) {
			currentTowers.add(newTowerToAdd);
			newTowerToAdd = null;
		}
	}
	
	// does the given rectangle intersect any towers?
	public boolean intersectsTowers(Rectangle r)
	{
		for (int i = 0; i < currentTowers.size(); i++) {
			if (r.intersects(currentTowers.get(i).getRectangle()))
				return true;
		}
		return false;
	}
}
