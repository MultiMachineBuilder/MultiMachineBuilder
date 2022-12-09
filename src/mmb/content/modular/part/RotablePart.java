/**
 * 
 */
package mmbgame.modular.part;

import java.awt.image.BufferedImage;

import mmbeng.rotate.RotatedImageGroup;

/**
 * @author oskar
 *
 */
public class RotablePart extends Part implements RotablePartEntry {
	private RotatedImageGroup rig;
	@Override
	public void setTexture(BufferedImage texture) {
		rig = RotatedImageGroup.create(texture);
		super.setTexture(texture);
	}
	public RotablePart setTexture(RotatedImageGroup texture) {
		rig = texture;
		super.setTexture(texture.U);
		return this;
	}
	/** @return the rotated texture */
	@Override
	public RotatedImageGroup rig() {
		return rig;
	}
}
