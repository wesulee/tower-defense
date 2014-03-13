package towerdefense.animation;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;

public class AnimationContainer
{
	private final LinkedList<Animation> list;
	
	public AnimationContainer()
	{
		list = new LinkedList<Animation>();
	}
	
	public void update()
	{
		if (!list.isEmpty()) {
			Iterator<Animation> iter = list.iterator();
			while (iter.hasNext()) {
				if (iter.next().update())
					iter.remove();
			}
		}
	}
	
	public void draw(Graphics2D g)
	{
		for (Animation anim : list)
			anim.draw(g);
	}
	
	public void add(Animation anim) {list.add(anim);}
}
