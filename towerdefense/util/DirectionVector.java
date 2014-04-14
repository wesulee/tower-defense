package towerdefense.util;

public class DirectionVector
{
	private final double x;
	private final double y;
	private final double length;
	
	public DirectionVector(double x, double y)
	{
		final double div = Utility.length(x,  y);
		this.x = x / div;
		this.y = y / div;
		this.length = 1.0 ;
	}
	
	public DirectionVector(double x, double y, boolean norm)
	{
		if (norm) {
			final double div = Utility.length(x,  y);
			this.x = x / div;
			this.y = y / div;
			this.length = 1.0;
		}
		else {
			this.x = x;
			this.y = y;
			this.length = Utility.length(x,  y);
		}
	}
	
	public double getX() {return x;}
	public double getY() {return y;}
	public double length() {return length;}
	
	public Direction toDirection()
	{
		double degrees = Math.toDegrees(Math.atan2(y, x));
		if (degrees < 0)
			degrees += 360.0;
		
		// round degrees to nearest multiple of 90
		int deg = (int) (90 * Math.round(degrees / 90)) % 360;
		
		Direction ret = Direction.N;	// default value
		switch (deg) {
		case 0:
			ret = Direction.E;
			break;
		case 90:
			ret = Direction.S;
			break;
		case 180:
			ret = Direction.W;
			break;
		case 270:
			ret = Direction.N;
			break;
		}
		return ret;
	}
}
