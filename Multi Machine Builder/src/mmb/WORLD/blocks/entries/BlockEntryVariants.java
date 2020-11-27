/**
 * 
 */
package mmb.WORLD.blocks.entries;

import mmb.WORLD.blocks.VariantHandler;
import mmb.WORLD.blocks.defs.BlockDef;

/**
 * @author oskar
 * @param <T> type of input enumeration
 *
 */
public class BlockEntryVariants<T extends Enum<?>&VariantHandler> {
	public BlockDef block;
	private T value;
}
