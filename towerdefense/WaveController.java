package towerdefense;

import java.awt.Rectangle;

import towerdefense.GameStates.RunningGame;
import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureContainer;
import towerdefense.creatures.CreatureType;

/**
 * Manages when waves occur and what creatures spawn.
 * Spawns creatures by updating spawn queue of CreatureContainer.
 * update should be consistently called.
 */
public class WaveController
{
	private final int waveCooldown = 5;	// sec between waves
	private int waveNumber = 0;
	// is the game currently on wave cooldown?
	private boolean inCooldown = true;
	private long lastWaveTime;
	private final long waveCooldownNano;
	private Wave currentWave = null;
	private final RunningGame gp;
	private final CreatureContainer creatures;
	// used to set initial position for creature
	private final Rectangle spawnRect;
	
	
	public WaveController(RunningGame gp)
	{
		waveCooldownNano = waveCooldown * 1000000L;
		lastWaveTime = System.nanoTime();
		
		this.gp = gp;
		this.creatures = gp.getCreatureContainer();
		spawnRect = gp.getMap().getSpawnRectangle();
	}
	
	public void update(long time)
	{
		if (inCooldown) {
			// see if time for new wave
			if (time - lastWaveTime > waveCooldownNano) {
				currentWave = newWave(++waveNumber);
				creatures.setSpawnQueue(currentWave.getCreatureList());
				inCooldown = false;
			}
		}
	}
		
	// all creatures from previous wave killed, begin cooldown
	public void notifyWaveFinished()
	{
		if (!inCooldown) {
			lastWaveTime = System.nanoTime();
			inCooldown = true;
		}
	}
	
	// generate a simple wave
	public Wave newWave(int level)
	{
		CreatureType ct = CreatureType.Spider;
		Wave nw = new Wave();
		// initial creature position is middle of spawn rectangle
		int x = spawnRect.x + spawnRect.width / 2;
		int y = spawnRect.y + spawnRect.height / 2;
		for (int i = 0; i < level; i++) {
			Creature c = Creature.newCreature(ct, x, y);
			nw.addCreature(c);
		}
		return nw;
	}
	
	public int getWaveNumber() {return waveNumber;}
}
