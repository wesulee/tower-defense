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
		g.setFont(font);
		g.setColor(Color.gray);
		g.fillRect(WIDTH_OFFSET, 0, WIDTH, HEIGHT);
		g.setColor(Color.white);
		
		g.drawString("Wave "+wave, WIDTH_OFFSET+20, 20);
		
		// display health and gold
		g.drawString("Health: " + player.getHealth() + "/" + playerMaxHealth, 
				WIDTH_OFFSET+15, HEIGHT-45);
		g.drawString("Gold: " + player.getGold(), WIDTH_OFFSET+15, HEIGHT-25);
	}
	
	public void setWaveNumber(int n) {wave = n;}
}
