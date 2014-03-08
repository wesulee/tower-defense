package towerdefense.ui;

import java.awt.image.BufferedImage;

/**
 * An icon that represents some entity.
 */
public class GenericIcon<T> extends Icon
{
	private final T entity;
	
	public GenericIcon(BufferedImage img, int pos_x, int pos_y,
			T entity)
	{
		super(img, pos_x, pos_y);
		this.entity = entity;
	}
	
	public T getEntity() {return entity;}
}
