/**
 * 
 */
package mmb.WORLD.block;

import java.util.Objects;
import java.util.function.Consumer;

import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.WORLD.items.Items;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Blocks {
	private static Debugger debug = new Debugger("BLOCKS");
	private static final SelfSet<String, BlockType> blocks = new HashSelfSet<>();
	
	public static void register(BlockType type) {
		Objects.requireNonNull(type, "The block must not be null");
		Objects.requireNonNull(type.id, "The block's ID can't be null");
		StringBuilder sb = new StringBuilder();
		sb.append("Adding ").append(type.id).append(" with title ").append(type.title).append(" and descripion:\n").append(type.description);
		debug.printl(sb.toString());
		blocks.add(type);
	}
	
	public static void remove(BlockType typ) {
		Items.remove(typ);
		blocks.remove(typ);
		
	}
	/**
	 * Get a block with following ID, or null if block with given ID is not found
	 * @param name ID of the block
	 * @return a block with given name, or null if not found
	 */
	public static BlockType get(String name) {
		return blocks.get(name);
	}
	/**
	 * Remove given block by name
	 * @param s block name
	 */
	public static void remove(String s) {
		Objects.requireNonNull(s, "Block name can't be null");
		debug.printl("Removing "+s);
		blocks.remove(s);
		Items.remove(s);
	}
	public static void forEach(Consumer<BlockType> action) {
		blocks.forEach((b) -> action.accept(b));
	}
	
	public static BlockType[] getBlocks() {
		return blocks.toArray(new BlockType[blocks.size()]);
	}
}
