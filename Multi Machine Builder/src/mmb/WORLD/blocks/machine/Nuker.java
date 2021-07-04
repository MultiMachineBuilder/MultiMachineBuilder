/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.Component;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 * Represents a nuclear reactor
 */
public class Nuker extends SkeletalBlockMachine {
	@Override
	protected void onTick0(MapProxy map) {
		// TODO Auto-generated method stub
		super.onTick0(map);
	}

	@Override
	protected Component createGUI() {
		// TODO Auto-generated method stub
		return super.createGUI();
	}

	private ItemEntry currNuked;
	public Nuker(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2, 15);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.NUKEGEN;
	}

	@Override
	public void load1(ObjectNode data) {
		currNuked = ItemEntry.loadFromJson(data.get("fuel"));
	}

	@Override
	protected void save1(ObjectNode node) {
		node.set("fuel", ItemEntry.saveItem(currNuked));
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
