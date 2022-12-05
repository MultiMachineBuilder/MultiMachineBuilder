/**
 * 
 */
package mmbgame.modular.part;

import mmbeng.rotate.RotatedImageGroup;

/**
 * A part with texture, which can be rotated
 * @author oskar
 *
 */
public interface RotablePartEntry {
	/** @return the rotated texture */
	public RotatedImageGroup rig();
}
