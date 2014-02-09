package towerdefense;

import java.util.ArrayList;

import towerdefense.creatures.Creature;
import towerdefense.creatures.CreatureType;

/**
 * General Usage:
 * Populate a wave by adding Creatures. Once finalized, pass instance
 * to appropriate class to spawn Creatures in order they were added.
 */
public class Wave
{
	private ArrayList<Creature> creatureList;
	
	public Wave() {creatureList = new ArrayList<Creature>();}
	
	public void addCreature(Creature c) {creatureList.add(c);}
	
	public Creature getNextCreature()
	{
		if (creatureList.isEmpty()) {
			return null;
		}
		else {
			return creatureList.remove(0);
		}
	}
	
	public CreatureType nextCreatureType()
	{
		if (creatureList.isEmpty()) {
			return null;
		}
		else {
			return creatureList.get(0).getType();
		}
		
	}
	public boolean isEmpty() {return creatureList.isEmpty();}
	
	public ArrayList<Creature> getCreatureList() {return creatureList;}
}
