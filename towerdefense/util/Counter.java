package towerdefense.util;

import towerdefense.GamePanel;

public class Counter
{
	private final int maxTicks;
	private int ticks = 0;
	
	public Counter(int ms)
	{
		long milliToNano = ms * 1000000L;
		this.maxTicks = (int) (milliToNano / GamePanel.period) + 1;
	}
	
	public boolean update(){return (++ticks > maxTicks);}
	public int getTicks() {return ticks;}
	public int getMaxTicks() {return maxTicks;}
	public void reset() {ticks = 0;}
}