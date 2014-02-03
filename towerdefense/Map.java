package towerdefense;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

import towerdefense.creatures.TestCreature;

/**
 * Map is used only to contain Wave data and the background sprite.
 */
public class Map
{
	private BufferedImage backgroundSprite;
	private long lastWaveTime;
	private final int waveCooldown = 15;
	private final long waveCooldownNano;
	
	public Map()
	{
		waveCooldownNano = 15 * 1000000L;
		String fname = "Resources/Maps/test_map.png";
		try {
			InputStream is = TowerDefense.class.getResourceAsStream(fname);
			backgroundSprite = ImageIO.read(is);
		} 
		catch (Exception e) {
			System.out.println("Unable to load " + fname);
			System.exit(1);
		}
		lastWaveTime = System.nanoTime() - waveCooldownNano;
	}
	
	public void draw(Graphics2D g) {g.drawImage(backgroundSprite, 0, 0, null);}
	
	public Wave getWave(int n)
	{
		Wave newWave = new Wave();
		newWave.addCreature(new TestCreature(500, 500));
		return newWave;
	}
	
	// the wave has been completed, begin cooldown to next wave
	public void notifyWaveFinished(long time)
	{
		lastWaveTime = time;
	}
	
	// is it time for another wave?
	public boolean nextWaveAvailable()
	{
		return true;
	}
}
