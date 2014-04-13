package towerdefense.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.EnumMap;

import towerdefense.GamePanel;
import towerdefense.components.Player;
import towerdefense.gamestates.RunningGame;
import towerdefense.towers.TowerType;

/**
 * Menu layout in vertical order: wave, icons, description, health/gold
 */
public final class Menu 
{
	public final int WIDTH = GamePanel.WIDTH - RunningGame.MENU_X;
	public final int HEIGHT = GamePanel.HEIGHT;
	public final int X_START = RunningGame.MENU_X;
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
	private final int DESC_PADDING = 5;
	private final int DESC_X1 = X_START + DESC_PADDING;
	private final int DESC_X2 = X_START + WIDTH - DESC_PADDING;
	private final int DESC_Y1 = 200;
	
	private final RunningGame rg;
	private final Player player;
	private Font font = GamePanel.defaultFont;
	// type that mouse clicked on, activated only if sufficient funds
	private TowerType typeSelected = null;
	private final EnumMap<TowerType, TextBox> tDescMap;
	private TextBox descToDraw = null;
	private final TowerIconLayout iconLayout;
	
	public Menu(RunningGame rg, Player player)
	{
		this.rg = rg;
		this.player = player;
		this.tDescMap = new EnumMap<TowerType, TextBox>(TowerType.class);
		this.iconLayout = new TowerIconLayout(this);
		
		Graphics2D g = (Graphics2D)rg.getGamePanel().getGraphics();
		
		for (TowerType tt : TowerType.values())
			tDescMap.put(tt, new TextBox(
					tt.getDescription(), DESC_X1, DESC_X2, DESC_Y1, g));
		
		g.dispose();
	}
	
	public void draw(final Graphics2D g)
	{		
		g.setColor(BG_COLOR);
		g.fillRect(X_START, 0, WIDTH, HEIGHT);
				
		g.setFont(font);
		g.setColor(Color.white);
		
		g.drawString("Wave " + rg.getWaveController().getWaveNumber(),
				X_START + 20, 20);
		
		// display health and gold
		g.drawString("Health: " + player.getHealth(),
				X_START + 15, HEIGHT - 45);
		g.drawString("Gold: " + player.getGold(), X_START + 15, HEIGHT - 25);
		
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
	
	public void mouseMoved(final int x, final int y)
	{
		if (iconLayout.insideIcon(x, y)) {
			rg.setCurrentCursor(Cursor.HAND_CURSOR);
		}
		else {
			rg.setCurrentCursor(Cursor.DEFAULT_CURSOR);
		}
	}
	
	public void mouseClicked(final int x, final int y)
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
	
	// for layout error notification
	public GamePanel getGamePanel() {return rg.getGamePanel();}
}
