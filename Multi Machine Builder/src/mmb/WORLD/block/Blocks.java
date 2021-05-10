/**
 * 
 */
package mmb.WORLD.block;

import java.util.Objects;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.item.Items;
import mmb.debug.Debugger;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Blocks {
	private static Debugger debug = new Debugger("BLOCKS");
	private static final SelfSet<String, BlockType> _blocks = new HashSelfSet<>();
	/**
	 * An unmodifiable {@link SelfSet} of all {@link BlockType}s
	 */
	@SuppressWarnings("null")
	public static final SelfSet<String, BlockType> blocks = Collects.unmodifiableSelfSet(_blocks);
	
	@SuppressWarnings("null")
	public static void register(BlockType type) {
		Objects.requireNonNull(type, "The block must not be null");
		Objects.requireNonNull(type.id(), "The block's ID can't be null");
		StringBuilder sb = new StringBuilder();
		sb.append("Adding ").append(type.id())
		.append(" with title ").append(type.title())
		.append(" and descripion:\n").append(type.description());
		debug.printl(sb.toString());
		Items.register(type);
		_blocks.add(type);
	}
	
	public static void remove(BlockType typ) {
		Items.remove(typ);
		_blocks.remove(typ);
		
	}
	/**
	 * Get a block with following ID, or null if block with given ID is not found
	 * @param name ID of the block
	 * @return a block with given name, or null if not found
	 */
	public static BlockType get(String name) {
		return _blocks.get(name);
	}
	/**
	 * Remove given block by name
	 * @param s block name
	 */
	public static void remove(String s) {
		Objects.requireNonNull(s, "Block name can't be null");
		debug.printl("Removing "+s);
		_blocks.removeKey(s);
		Items.remove(s);
	}

	public static BlockType[] getBlocks() {
		return _blocks.toArray(new BlockType[_blocks.size()]);
	}

	public static boolean isGround(BlockEntry ent) {
		return ent == ContentsBlocks.air || ent == ContentsBlocks.grass;
	}
	public static boolean isGround(BlockType ent) {
		return ent == ContentsBlocks.air || ent == ContentsBlocks.grass;
	}
}
