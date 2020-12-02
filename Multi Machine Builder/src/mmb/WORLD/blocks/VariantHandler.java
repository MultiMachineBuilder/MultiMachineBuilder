/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public interface VariantHandler<T extends Enum<T>> {
	/**
	 * @return block's variant display name
	 */
	public String variantName();
	public BlockDrawer texture();
}
