public class Tower
{
	private String name;
	private float damage;		// damage per attack
	private int range;			// distance tower can hit target
	private float speed;		// attacks per second
	private int size;			// radius of tower
	private long lastAttack;
	private long attackPeriod;	// delay between attacks, ns
	private float pos_x;		// tower position, x
	private float pos_y;		// tower position, y
	
	public Tower(float damage, int range, float speed, int size,
			float pos_x, float pos_y)
	{
		this.damage = damage;
		this.range = range;
		this.speed = speed;
		this.size = size;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		attackPeriod = (long)(1000.0 / speed) * 1000000L;
		// tower can attack immediately after created
		lastAttack = System.nanoTime() - attackPeriod;
	}
	
	public boolean canAttack(long time)
	{
		return time - lastAttack > attackPeriod;
	}
	
	public void setLastAttack(long time) {lastAttack = time;}
	public float getDamage() {return damage;}
	public int size() {return size;}
}