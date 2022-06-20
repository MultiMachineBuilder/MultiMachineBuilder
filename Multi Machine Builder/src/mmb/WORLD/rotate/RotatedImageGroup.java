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
	static BufferedImage rotate(BufferedImage img) {
		int type = img.getType();
		if(type == 0) type = 5;
		BufferedImage result = new BufferedImage(32, 32, type);
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 32; j++) {
				result.setRGB(31-j, i, img.getRGB(i, j));
			}
		}
		return result;
	}
}
