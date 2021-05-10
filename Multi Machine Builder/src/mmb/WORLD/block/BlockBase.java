/**
 * 
 */
package mmb.WORLD.block;

import java.util.Objects;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.item.Item;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 * Shared implementation for {@link Block} and {@link BlockEntityType}. More types will be added in future
 */
public abstract class BlockBase extends Item implements BlockType {
	@Deprecated
	public BlockType leaveBehind;
	@Deprecated
	public BlockDrawer texture;
	private Drop drop;

	@Override
	public void openGUI(WorldWindow window) {
		//unused
	}

	@Override
	public void closeGUI(WorldWindow window) {
		//unused
	}

	@Override
	public void setLeaveBehind(BlockType block) {
		this.leaveBehind = block;
	}
	@Override
	public BlockType leaveBehind() {
		return leaveBehind;
	}
	@Override
	public Drop getDrop() {
		return drop;
	}
	@Override
	public void setDrop(Drop drop) {
		this.drop = Objects.requireNonNull(drop, "drop is null");
	}

	@Override
	public void register() {
		if(leaveBehind == null) leaveBehind = ContentsBlocks.grass; //NOSONAR
		if(drop == null) drop = (i, m, x, y) -> Drop.tryDrop(this, i, m, x, y);
		Blocks.register(this);
	}
	@Override
	public void register(String id) {
		this.id = id;
		register();
	}
	
}
