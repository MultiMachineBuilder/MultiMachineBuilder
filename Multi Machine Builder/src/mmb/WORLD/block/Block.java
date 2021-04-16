/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.item.Item;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class Block extends Item implements BlockEntry, BlockType{
	
	@Override
	public JsonNode save() {
		return new TextNode(id);
	}
	@Override
	public BlockType type() {
		return this;
	}
	@Override
	public void place(int x, int y, World map) {
		map.place(this, x, y);
	}

	@Override
	public BlockEntry create(int x, int y, BlockMap map) {
		return this;
	}

	//Placement GUI
	/**
	 * @deprecated Use {@link #openGUI(WorldWindow)} instead
	 */
	@Override
	public void openGUI() {
		openGUI(null);
	}
	//Placement GUI
	@Override
	public void openGUI(WorldWindow window) {
		//no GUI available
	}
	/**
	 * @deprecated Use {@link #closeGUI(WorldWindow)} instead
	 */
	@Override
	public void closeGUI() {
		closeGUI(null);
	}
	@Override
	public void closeGUI(WorldWindow window) {
		//no GUI available
	}
	
	//Leave behind
	public BlockType leaveBehind;
	@Override
	public void setLeaveBehind(BlockType block) {
		leaveBehind = block;
	}
	@SuppressWarnings("null")
	@Override
	public BlockType leaveBehind() {
		return leaveBehind;
	}
	
	//Placer preview
	@Override
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		drawer.draw(this, renderStartPos, g);
	}

	//Not a BlockEntity
	@Override
	public boolean isBlockEntity() {
		return false;
	}
	@Override
	public BlockEntity asBlockEntity() {
		throw new IllegalStateException("Not a BlockEntity");
	}
	@Override
	public BlockEntityType asBlockEntityType() {
		throw new IllegalStateException("Not a BlockEntity");
	}
	@Override
	public BlockEntityType nasBlockEntityType() {
		return null;
	}
	@Override
	public BlockEntity nasBlockEntity() {
		return null;
	}
	
	//Register
	@Override
	public void register() {
		if(leaveBehind == null) leaveBehind = ContentsBlocks.grass; //NOSONAR
		Blocks.register(this);
	}
	@Override
	public void register(String id) {
		this.id = id;
		register();
	}
	
	private Drop drop;
	@Override
	public Drop getDrop() {
		if(drop == null) drop = this;
		return drop;
	}
	@Override
	public void setDrop(Drop drop) {
		this.drop = Objects.requireNonNull(drop, "drop is null");
	}
}
