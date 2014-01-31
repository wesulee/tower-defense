import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.io.IOException;

//container for all tower sprites
public class TowerSprites
{
	private static ArrayList<BufferedImage> sprites;
	
	public TowerSprites()
	{
		sprites = new ArrayList<BufferedImage>();
		
		for (TowerType tt : TowerType.values()) {
			BufferedImage img = loadSprite(tt.getFileName());
			if (img == null) {
				// unable to load tower sprite...exit
				System.exit(1);
			}
			else {
				sprites.add(img);
			}
		}
	}
	
	public static BufferedImage getSprite(TowerType tt)
	{
		int i = 0;
		for (TowerType _tt : TowerType.values()) {
			if (tt == _tt) {
				return sprites.get(i);
			}
			i++;
		}
		return null;
	}
	
	// initial sprite loading
	private BufferedImage loadSprite(String fname)
	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("/Resources/Towers/"+fname));
		} 
		catch (IOException e) {
			System.out.println("Unable to load /Resources/Towers/"+fname);
		}
		return img;
	}
}
