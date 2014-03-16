package towerdefense.towers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import towerdefense.GamePanel;
import towerdefense.SpriteContainer;
import towerdefense.creatures.CreatureContainer;
import towerdefense.gamestates.RunningGame;
import towerdefense.projectiles.ProjectileContainer;

/**
 * All game towers should be stored in this class
 */
public class TowerContainer
{
	private final RunningGame rg;
	private final ProjectileContainer pc;
	
	private final int drawLimitX;	// do not draw towers past this
	private ArrayList<Tower> currentTowers;
	private Tower newTowerToAdd = null;
	// player clicked on this existing tower
	private Tower gameSelectedTower = null;
	
	// player selected this type of tower from menu
	private TowerType menuSelectedTower = null;
	private BufferedImage menuSelectedTowerSprite = null;
	
	public TowerContainer(RunningGame rg, int drawLimitX)
	{
		this.rg = rg;
		this.pc = new ProjectileContainer();
		this.drawLimitX = drawLimitX;
		currentTowers = new ArrayList<Tower>();
		//add(TowerType.TestTowerType, 541, 226);
		//processChanges();
		//add(TowerType.TestTower2Type, 600, 226);
		//add(TowerType.TestTower2Type, 541, 299);
	}
	
	public void update(long time, CreatureContainer cc)
	{
		processChanges();
		
		for (Tower t : currentTowers) {
			if (t.canAttack(time)) {
				t.attack(time, cc.getCreaturesNear(
						t.getX(), t.getY(), t.getRange()));
			}
		}
		
		pc.update();
	}
	
	public void draw(Graphics2D g)
	{
		if (gameSelectedTower != null) {
			gameSelectedTower.drawRangeCircle(g);
		}
		
		for (Tower t : currentTowers) {
			t.draw(g);
		}
		
		if (rg.getMenu().towerIsSelected() &&
				(rg.getMouseX() < drawLimitX)) {
			Tower.drawRangeCircleWithTower(g, menuSelectedTowerSprite, 
					rg.getMouseX(), rg.getMouseY(),
					menuSelectedTower.getRange());
		}
		
		pc.draw(g);
	}
	
	public ProjectileContainer getProjectileContainer() {return pc;}
	public RunningGame getGameState() {return rg;}
	
	public void setMenuSelectedTower(TowerType tt)
	{
		menuSelectedTower = tt;
		menuSelectedTowerSprite = SpriteContainer.getSprite(tt);
	}
	
	public void clearMenuSelectedTower() {menuSelectedTower = null;}
	public boolean menuTowerIsSelected() {return menuSelectedTower != null;}
	
	// assumes tower can be built at given location
	// should call update between adding multiple towers
	public void add(TowerType tt, int x, int y)
	{
		Tower t = TowerFactory.newTower(rg, tt, x, y);
		newTowerToAdd = t;
	}
	
	private void processChanges()
	{
		if (newTowerToAdd != null) {
			currentTowers.add(newTowerToAdd);
			newTowerToAdd = null;
		}
	}
	
	// does the given rectangle intersect any towers?
	private boolean intersectsTowers(Rectangle r)
	{
		for (int i = 0; i < currentTowers.size(); i++) {
			if (r.intersects(currentTowers.get(i).getRectangle()))
				return true;
		}
		return false;
	}
	
	public boolean canAddTower(TowerType tt, int x, int y)
	{
		int size = tt.getSize();
		Rectangle r = new Rectangle(x-size, y-size, size*2, size*2);
		return rg.getMap().spotAvailable(r) && !intersectsTowers(r);
	}
	
	public Tower getTowerAt(int x, int y)
	{
		for (Tower t : currentTowers) {
			if (getDistance(x, y, t) < t.getSize())
				return t;
		}
		return null;
	}
	
	// get the distance between t and (x, y)
	private double getDistance(int x, int y, Tower t)
	{
		int dx = t.getX() - x;
		int dy = t.getY() - y;
		return Math.sqrt(dx*dx + dy*dy);
	}
}
