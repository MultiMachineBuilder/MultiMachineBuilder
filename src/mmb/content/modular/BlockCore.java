/**
 * 
 */
package mmb.content.modular;

/**
 * A block core
 * @author oskar
 * @param <Tcore> type of this core
 */
public interface BlockCore<Tcore extends BlockCore<Tcore>> extends BlockModuleOrCore<Tcore, ModularBlock<?, ?, Tcore, ?>, ModularBlock<?, ?, Tcore, ?>> {
	/**
	 * Runs the core
	 * @param block the block running this module
	 */
	public default void runCore(ModularBlock<?, ?, Tcore, ?> block) {
		//unused
	}
}
