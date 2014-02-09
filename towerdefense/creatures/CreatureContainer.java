package towerdefense.creatures;

import java.awt.Rectangle;
import java.util.ArrayList;

import towerdefense.GamePanel;
import towerdefense.Map;
import towerdefense.Player;

public class CreatureContainer
{
	private ArrayList<Creature> creatures;
	private ArrayList<Creature> spawnQueue;
	// most recent creature spawned
	private Creature spawnCreature = null;
	private final Rectangle spawnRect;
	
	private final GamePanel gp;
	private final Map map;
	private final Player player;
	
	public CreatureContainer(GamePanel gp)
	{
		this.gp = gp;
		this.map = gp.getMap();
		this.player = gp.getPlayer();
		this.spawnRect = map.getSpawnRectangle();
		
		creatures = new ArrayList<Creature>();
		spawnQueue = new ArrayList<Creature>();
	}

	public void update()
	{
		Creature c;
		// try to spawn creature
		if (!spawnQueue.isEmpty() && (spawnCreature == null) ||
				!spawnCreature.getRectangle().intersects(spawnRect)) {
			c = spawnQueue.get(0);
			// not implemented
		}
		
		// remove dead creatures, update positions
		for (int i = 0; i < creatures.size(); i++) {
			c = creatures.get(i);
			// is creature dead?
			if (c.getHealth() <= 0) {
				player.increaseGold(c.getGoldDrop());
				creatures.remove(i);
				i--;
			}
			// update positions
			else {
				// not implemented
			}
		}
	}
	
	public void setSpawnQueue(ArrayList<Creature> spawn)
	{
		spawnQueue = spawn;
	}
	
	// no other class should modify size of creatures
	public ArrayList<Creature> getCreatureList() {return creatures;}
}
