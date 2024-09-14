/**
 * 
 */
package mmb.engine.rotate;

import java.awt.image.BufferedImage;

import mmb.NN;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.texture.Textures;

/**
 * An image group with chirotation (4 rotation * 2 chiralities = 8 images)
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

	public BlockDrawer Ul;
	public BlockDrawer Dl;
	public BlockDrawer Ll;
	public BlockDrawer Rl;
	public BlockDrawer Ur;
	public BlockDrawer Dr;
	public BlockDrawer Lr;
	public BlockDrawer Rr;
	
	/**
	 * Creates a Chirotated Image Group out of an image
	 * @param img image to transform
	 * @return a chirotated image group
	 */
	@NN public static ChirotatedImageGroup create(BufferedImage img) {
		return create(img, flip(img));
	}
	/**
	 * Creates a Chirotated Image Group out of an image
	 * @param cw clockwise variant of the image
	 * @param ccw counter-clockwise variant of the image
	 * @return a chirotated image group
	 */
	@NN public static ChirotatedImageGroup create(BufferedImage cw, BufferedImage ccw) {
		ChirotatedImageGroup rig = new ChirotatedImageGroup();
		rig.Ur = BlockDrawer.ofImage(cw);
		BufferedImage progress = RotatedImageGroup.rotate(cw);
		rig.Rr = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Dr = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Lr = BlockDrawer.ofImage(progress);
		progress = ccw;
		rig.Ul = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Rl = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Dl = BlockDrawer.ofImage(progress);
		progress = RotatedImageGroup.rotate(progress);
		rig.Ll = BlockDrawer.ofImage(progress);
		return rig;
	}
	/**
	 * Creates a chirotated image group
	 * @param texture path to the texture
	 * @return a chirotated image group
	 */
	@NN public static ChirotatedImageGroup create(String texture) {
		return create(Textures.get(texture));
	}
	/**
	 * Creates a chirotated image group
	 * @param cw clockwise texture
	 * @param ccw counter-clockwise texture
	 * @return a chirotated image group
	 */
	@NN public static ChirotatedImageGroup create(String cw, String ccw) {
		return create(Textures.get(cw), Textures.get(ccw));
	}
	@NN static BufferedImage flip(BufferedImage img) {
		BufferedImage result = new BufferedImage(32, 32, img.getType());
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 32; j++) {
				result.setRGB(31-i, j, img.getRGB(i, j));
			}
		}
		return result;
	}
}
