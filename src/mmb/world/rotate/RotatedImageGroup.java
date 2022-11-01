/**
 * 
 */
package mmb.world.rotate;

import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.data.contents.Textures;
import mmb.graphics.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class RotatedImageGroup {
	@Nonnull public final BlockDrawer U, D, L ,R;
	public RotatedImageGroup(BlockDrawer u, BlockDrawer d, BlockDrawer l, BlockDrawer r) {
		U = u;
		D = d;
		L = l;
		R = r;
	}
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
		BlockDrawer U = BlockDrawer.ofImage(img);
		BufferedImage progress = rotate(img);
		BlockDrawer R = BlockDrawer.ofImage(progress);
		progress = rotate(progress);
		BlockDrawer D = BlockDrawer.ofImage(progress);
		progress = rotate(progress);
		BlockDrawer L = BlockDrawer.ofImage(progress);
		return new RotatedImageGroup(U, D, L, R);
	}
	@Nonnull public RotatedImageGroup flip() {
		return new RotatedImageGroup(D, U, R, L);
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
