import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class TestTower extends Tower implements BasicTower
{
	// default tower attributes
	private static final String name = "Test Tower";
	private static final double DAMAGE = 10.0;
	private static final int RANGE = 100;
	private static final double SPEED = 2.0;
	private static final int SIZE = 15;
	private static final int COST = 1;
	private static final TowerType tt = TowerType.TestTowerType;
	private static BufferedImage sprite;
	
	public TestTower(int pos_x, int pos_y)
	{
		super(DAMAGE, RANGE, SPEED, SIZE, COST, pos_x, pos_y);
		if (sprite == null) {
			sprite = TowerSprites.getSprite(tt);
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(sprite, getPositionX(), getPositionY(), null);
		return;
	}
	
	public TowerType getTowerType() {return tt;}
	public String getName() {return name;}

}
