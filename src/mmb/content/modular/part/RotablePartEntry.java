/**
 * 
 */
package mmb.content.modular.part;

import mmb.engine.rotate.RotatedImageGroup;

/**
 * A part with texture, which can be rotated
 * @author oskar
 *
 */
public interface RotablePartEntry {
	/** @return the rotated texture */
	public RotatedImageGroup rig();
}
