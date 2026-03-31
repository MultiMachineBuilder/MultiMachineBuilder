package mmb.beans;

import java.awt.image.BufferedImage;

import mmb.engine.blockdrawer.BlockDrawer;
import mmb.engine.blockdrawer.TextureDrawer;
import mmb.engine.texture.Textures;

/**
 * Assigns a texture to a block
 */
public interface ITexture {
	/** Gets the block's texture */
	public BlockDrawer getTexture();
	/** Sets the block's texture. Must not be null*/
	public void setTexture(BlockDrawer texture);
	public default void setTextureAsset(String texture) {
		setTexture(new TextureDrawer(Textures.get(texture)));
	}
	public default void setTextureImage(BufferedImage texture) {
		setTexture(new TextureDrawer(texture));
	}
}
