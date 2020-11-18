/**
 * 
 */
package mmb.world2.blockworld.blocks.factory;

import java.util.function.Consumer;

/**
 * @author oskar
 *
 */
public interface ModBlockSetupInfo<T super ModBlockSetupInfo> {
	public static T create();
}
