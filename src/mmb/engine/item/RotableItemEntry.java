/**
 * 
 */
package mmb.engine.item;

import mmb.engine.rotate.RotatedImageGroup;

/**
 * An item with texture, which can be rotated
 * @author oskar
 */
public interface RotableItemEntry extends ItemEntry{
	/** @return the rotated texture */
	public RotatedImageGroup rig();
}
