package towerdefense.projectiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import towerdefense.creatures.Creature;
import towerdefense.towers.Tower;
import towerdefense.util.Counter;
import towerdefense.util.DirectionVector;

public class ChainLightning implements Projectile
{
	private static final int jumps = 3;	// potentially hits jumps+1 Creatures
	// 95.4% of lightning nodes will be within this distance from
	// line between two objects
	private static final int toleranceLength = 20;
	// determines number of nodes (smaller is more jagged)
	private static final double distanceBetween = 5;
	private static final int duration = 85;	// ms
	
	private static final Random rand = new Random();
	private static final Stroke stroke = new BasicStroke(2);
	private static final double multiplier = (double) toleranceLength / 2;
	
	private final Counter counter = new Counter(duration);
	private final GeneralPath path;
	
	public ChainLightning(Tower source, Creature c)
	{
		final LinkedList<Double> points = new LinkedList<Double>();
		points.addAll(generatePath(source.getX(), source.getY(),
				c.getPositionX(), c.getPositionY()));
		points.add(c.getPositionX());
		points.add(c.getPositionY());
		c.decreaseHealth((int) source.getDamage());
		final Creature[] creatures = getChain(c);
		for (int i = 0; i < creatures.length-1; i++) {
			if ((creatures[i] == null) || (creatures[i+1] == null)) break;
			creatures[i+1].decreaseHealth((int) source.getDamage());
			points.addAll(generatePath(
					creatures[i].getPositionX(),
					creatures[i].getPositionY(),
					creatures[i+1].getPositionX(),
					creatures[i+1].getPositionY())
			);
			points.add(creatures[i+1].getPositionX());
			points.add(creatures[i+1].getPositionY());
		}
		
		this.path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.size()/2);
		path.moveTo(source.getX(), source.getY());
		ListIterator<Double> iter = points.listIterator();
		double x;
		double y;
		while (iter.hasNext()) {
			x = iter.next();
			y = iter.next();
			path.lineTo(x, y);
		}
	}

	public void draw(final Graphics2D g)
	{
		final Color oldColor = g.getColor();
		final Stroke oldStroke = g.getStroke();
		
		g.setStroke(stroke);
		g.setColor(Color.yellow);
		g.draw(path);
		
		g.setStroke(oldStroke);
		g.setColor(oldColor);
	}

	public boolean update() {return counter.update();}
	
	private Creature[] getChain(Creature c)
	{
		final Creature[] creatures = new Creature[jumps + 1];
		int i = 0;
		creatures[i++] = c;
		final LinkedList<Creature> allCreatures = Creature.cc.getCreatureList();
		final ListIterator<Creature> iter =
				allCreatures.listIterator(allCreatures.indexOf(c) + 1);
		while (iter.hasNext() && (i != creatures.length)) {
			creatures[i++] = iter.next();
		}
		
		return creatures;
	}
	
	// returns "random" {x3, y3, x4, y4, ...} BETWEEN (x1, y1) and (x2, y2)
	private LinkedList<Double> generatePath(double x1, double y1,
			double x2, double y2)
	{
		final DirectionVector between = new DirectionVector(x2-x1, y2-y1, false);
		final DirectionVector perp = 
				new DirectionVector(-between.getY(), between.getX());
		final int nodeCount = (int) (Math.round(between.length()
				/ distanceBetween) - 1);
		final LinkedList<Double> points = new LinkedList<Double>();
		final double xInc = between.getX() / (nodeCount + 1);
		final double yInc = between.getY() / (nodeCount + 1);
		double x = x1 + xInc;
		double y = y1 + yInc;
		for (final double mult : generateLengths(nodeCount)) {
			points.add(x + perp.getX() * mult);
			points.add(y + perp.getY() * mult);
			x += xInc;
			y += yInc;
		}
		return points;
	}
	
	// generate lengths of perp vectors
	private static double[] generateLengths(final int count)
	{
		final double[] doubles = new double[count];
		for (int i = 0; i < count; i++)
			doubles[i] = rand.nextGaussian() * multiplier;
		return doubles;
	}
}
