package towerdefense;

public class DirectionVector
{
	private static final boolean normalized = true;
	private double x;
	private double y;
	
	public DirectionVector()
	{
		x = 0.0;
		y = 1.0;
	}
	
	public DirectionVector(double x, double y)
	{
		if (normalized) {
			double length = Utility.length(x,  y);
			this.x = x / length;
			this.y = y / length;
		}
		else {
			this.x = x;
			this.y = y;
		}
		
	}
	
	public double getX() {return x;}
	public double getY() {return y;}
	
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
