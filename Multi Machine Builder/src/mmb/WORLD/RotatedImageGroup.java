/**
 * 
 */
package mmb.WORLD;

import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class RotatedImageGroup {
	public BlockDrawer U, D, L ,R;
	public BlockDrawer get(Rotation r) {
		switch(r) {
		case S:
			return D;
		case W:
			return L;
		case E:
			return R;
		default:
			return U;
		}
	}
	@Nonnull public static RotatedImageGroup create(BufferedImage img) {
		RotatedImageGroup rig = new RotatedImageGroup();
		rig.U = BlockDrawer.ofImage(img);
		BufferedImage progress = rotate(img);
		rig.R = BlockDrawer.ofImage(progress);
		progress = rotate(progress);
		rig.D = BlockDrawer.ofImage(progress);
		progress = rotate(progress);
		rig.L = BlockDrawer.ofImage(progress);
		return rig;
	}
	@Nonnull public static RotatedImageGroup create(String texture) {
		return create(Textures.get(texture));
	}
	private static BufferedImage rotate(BufferedImage img) {
		BufferedImage result = new BufferedImage(32, 32, img.getType());
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 32; j++) {
				result.setRGB(31-j, i, img.getRGB(i, j));
			}
		}
		return result;
	}
}
