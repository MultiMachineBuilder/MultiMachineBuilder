/**
 * 
 */
package mmb.WORLD.rotate;

import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.Textures;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ChirotatedImageGroup {

	/**
	 * @param rotation rotation of image
	 * @param chirality chirality of image
	 * @return the image with given rotation and chirality
	 */
	public BlockDrawer get(Rotation rotation, Chirality chirality) {
		switch(rotation) {
		case S:
			return (chirality == Chirality.L) ? Dl : Dr;
		case W:
			return (chirality == Chirality.L) ? Ll : Lr;
		case E:
			return (chirality == Chirality.L) ? Rl : Rr;
		default:
			return (chirality == Chirality.L) ? Ul : Ur;
		}
	}

	public BlockDrawer Ul, Dl, Ll, Rl, Ur, Dr, Lr, Rr;
	@Nonnull public static ChirotatedImageGroup create(BufferedImage img) {
		ChirotatedImageGroup rig = new ChirotatedImageGroup();
		rig.Ur = BlockDrawer.ofImage(img);
		BufferedImage progress = RotatedImageGroup.rotate(img);
		rig.Rr = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Dr = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Lr = BlockDrawer.ofImage(progress);
		progress = flip(img);
		rig.Ul = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Rl = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Dl = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Ll = BlockDrawer.ofImage(progress);
		return rig;
	}
	@Nonnull public static ChirotatedImageGroup create(String texture) {
		return create(Textures.get(texture));
	}
	static BufferedImage flip(BufferedImage img) {
		BufferedImage result = new BufferedImage(32, 32, img.getType());
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 32; j++) {
				result.setRGB(31-i, j, img.getRGB(i, j));
			}
		}
		return result;
	}
}
