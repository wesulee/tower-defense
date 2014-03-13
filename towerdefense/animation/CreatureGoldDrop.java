package towerdefense.animation;

import java.awt.Color;
import java.awt.Font;

import towerdefense.creatures.Creature;

public class CreatureGoldDrop extends VerticalTextScrollFade
{
	private static final Font font = new Font("SansSerif", Font.BOLD, 12);
	private static final Color color = Color.yellow;
	private static final int ms = 800;
	private static final int dy = -18;
	
	public CreatureGoldDrop(Creature c) {
		super(
				"+" + c.getGoldDrop(),
				(int)c.getPositionX(),
				(int)c.getPositionY(),
				(int)c.getPositionY() + dy,
				ms, font, color
		);
	}
}
