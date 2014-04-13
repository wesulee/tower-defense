package towerdefense.util;

public enum Direction
{
	N(0, "_n"), E(1, "_e"), S(2, "_s"), W(3, "_w");
	
	Direction(int i, String suffix)
	{
		this.i = i;
		this.suffix = suffix;
	}
	
	private final int i;
	private final String suffix;
	
	public int toIndex() {return i;}
	public String toSuffix() {return suffix;}
}
