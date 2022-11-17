/**
 * 
 */
package mmb.world.block;

import mmb.world.blocks.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class Blocks { //TODO remove, redundant with Items
	public static boolean isGround(BlockEntry ent) {
		return ent == ContentsBlocks.air || ent == ContentsBlocks.grass;
	}
	public static boolean isGround(BlockType ent) {
		return ent == ContentsBlocks.air || ent == ContentsBlocks.grass;
	}
}
