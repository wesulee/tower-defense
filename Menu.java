import java.awt.*;

// a vertically oriented menu
public class Menu 
{
	private final int WIDTH;
	private final int HEIGHT;
	private final int WIDTH_OFFSET;
	
	private final Player player;
	private final int playerMaxHealth;
	
	private int wave = 0;
	
	private Font font;
//	private FontMetrics metrics;
	
	
	public Menu(int width, int height, int width_offset, Player player)
	{
		WIDTH = width;
		HEIGHT = height;
		WIDTH_OFFSET = width_offset;
		this.player = player;
		playerMaxHealth = player.getMaxHealth();
		
		font = new Font("SansSerif", Font.BOLD, 12);
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(Color.gray);
		g.fillRect(WIDTH_OFFSET, 0, WIDTH, HEIGHT);
		
		// display health and gold
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Health: " + player.getHealth() + "/" + playerMaxHealth, 
				WIDTH_OFFSET+15, HEIGHT-45);
		g.drawString("Gold: " + player.getGold(), WIDTH_OFFSET+15, HEIGHT-25);
	}
	
	public void setWaveNumber(int n) {wave = n;}
}
