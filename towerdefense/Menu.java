package towerdefense;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.EnumMap;

import towerdefense.gamestates.RunningGame;
import towerdefense.towers.TowerType;
import towerdefense.ui.GenericIcon;
import towerdefense.ui.TextBox;
import towerdefense.ui.TowerIconLayout;

/**
 * Menu layout in vertical order: wave, icons, description, health/gold
 */
public class Menu 
{
	public final int WIDTH;
	public final int HEIGHT;
	public final int X_START;
	public final Color BG_COLOR = Color.gray;
	// distance from top of where icons will begin
	public final int ICON_Y_START = 50;
	public final int ICON_PADDING_X = 2;
	// padding below an icon
	public final int ICON_PADDING_Y = 2;
	// size of the local icon bitmap
	public final int ICON_SIZE = 30;
	public final int ICON_MARGIN = 3;
	// number of icons per row
	public final int ICON_COLUMNS = 3;
	public final Color ICON_BG_COLOR = Color.white;
	public final Color ICON_OUTLINE_COLOR = Color.black;
	// positions of rectangle that description will be drawn
	private final int DESC_X1;
	private final int DESC_X2;
	private final int DESC_Y1;
	
	private final RunningGame rg;
	private final Player player;
	private Font font;
	// type that mouse clicked on, activated only if sufficient funds
	private TowerType typeSelected = null;
	private final EnumMap<TowerType, TextBox> tDescMap;
	private TextBox descToDraw = null;
	private final TowerIconLayout iconLayout;
	
	public Menu(int x_offset, RunningGame rg, Player player)
	{
		this.WIDTH = GamePanel.WIDTH - x_offset;
		this.HEIGHT = GamePanel.HEIGHT;
		this.X_START = x_offset;
		this.rg = rg;
		this.player = player;
		this.font = new Font("SansSerif", Font.BOLD, 12);
		this.tDescMap = new EnumMap<TowerType, TextBox>(TowerType.class);
		this.DESC_X1 = X_START + 5;
		this.DESC_X2 = X_START + WIDTH - 10;
		this.DESC_Y1 = 200;
		
		iconLayout = new TowerIconLayout(this);
		
		for (TowerType tt : TowerType.values())
			tDescMap.put(tt, new TextBox(
					tt.getDescription(), DESC_X1, DESC_X2, DESC_Y1));
	}
	
	public void draw(Graphics2D g)
	{
		int gold = player.getGold();
		
		g.setColor(BG_COLOR);
		g.fillRect(X_START, 0, WIDTH, HEIGHT);
				
		g.setFont(font);
		g.setColor(Color.white);
		
		g.drawString("Wave " + rg.getWaveController().getWaveNumber(),
				X_START + 20, 20);
		
		// display health and gold
		g.drawString("Health: " + player.getHealth(),
				X_START + 15, HEIGHT - 45);
		g.drawString("Gold: " + gold, X_START + 15, HEIGHT - 25);
		
		iconLayout.draw(g);
		
		if (descToDraw != null)
			descToDraw.draw(g);
	}
	
	public TowerType getSelectedTower() {return typeSelected;}
	
	// the player's gold has been changed, update available towers
	public void notifyGoldChange()
	{
		iconLayout.recalcAvailable(player.getGold());
	}
	
	public void notifyMouseMoved(int x, int y)
	{
		if (iconLayout.insideIcon(x, y)) {
			rg.setCurrentCursor(Cursor.HAND_CURSOR);
		}
		else {
			rg.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		}
	}
	
	public void notifyMouseClicked(int x, int y)
	{
		GenericIcon<TowerType> icon = iconLayout.getIcon(x, y);
		if (icon == null) {
			if (typeSelected != null) {
				rg.getTowerContainer().clearMenuSelectedTower();
			}
			typeSelected = null;
			return;
		}
		// only set typeSelected when player has money for tower
		// and tower is different from current typeSelected
		if ((icon.getEntity().getCost() <= player.getGold()) &&
				(typeSelected != icon.getEntity())) {
			typeSelected = icon.getEntity();
			descToDraw = tDescMap.get(typeSelected);
			rg.getTowerContainer().setMenuSelectedTower(typeSelected);
		}
	}
	
	// is a tower currently selected from menu?
	public boolean towerIsSelected() {return typeSelected != null;}
	
	// player is no longer selecting a tower
	public void clearSelectedTower() {typeSelected = null;}
}
