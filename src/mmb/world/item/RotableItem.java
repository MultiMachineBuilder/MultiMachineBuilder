/**
 * 
 */
package mmb.world.item;

import java.awt.image.BufferedImage;

import mmb.graphics.texture.BlockDrawer;
import mmb.world.rotate.RotatedImageGroup;

/**
 * @author oskar
 *
 */
public class RotableItem extends Item {
	private RotatedImageGroup rig;
	@Override
	public void setTexture(BufferedImage texture) {
		rig = RotatedImageGroup.create(texture);
		super.setTexture(texture);
	}
	/** @return the rotated texture */
	public RotatedImageGroup rig() {
		return rig;
	}
}
