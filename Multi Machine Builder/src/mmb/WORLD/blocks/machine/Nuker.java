/**
 * 
 */
package mmb.WORLD.blocks.machine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 * Represents a nuclear reactor
 */
public class Nuker extends SkeletalBlockEntityData {
	private ItemEntry currNuked;
	public Nuker(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.NUKEGEN;
	}

	@Override
	public void load(JsonNode data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void save0(ObjectNode node) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return nuclear fuel currently in use
	 */
	public ItemEntry getCurrNuked() {
		return currNuked;
	}

	/**
	 * @param currNuked nuclear fuel to insert
	 */
	public void setCurrNuked(ItemEntry currNuked) {
		this.currNuked = currNuked;
	}

}
