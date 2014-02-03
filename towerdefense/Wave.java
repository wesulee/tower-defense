package towerdefense;

import java.util.ArrayList;

import towerdefense.creatures.Creature;

/**
 * General Usage:
 * Populate a wave by adding Creatures. Once finalized, pass instance
 * to appropriate class to spawn Creatures in order they were added.
 */
public class Wave
{
	private ArrayList<Creature> creatureList;
	
	public Wave()
	{
		creatureList = new ArrayList<Creature>();
	}
	
	public void addCreature(Creature c) {creatureList.add(c);}
	public Creature getCreature() {return creatureList.get(0);}
	public boolean isEmpty() {return creatureList.isEmpty();}
}
