/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.WORLD.Side;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.NoSuchInventory;
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
	@Override
	public void openGUI() {
		//no GUI available
	}
	@Override
	public void closeGUI() {
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
	public BufferedImage getIcon() {
		return drawer.img;
	}
	@Override
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		drawer.draw(renderStartPos, g);
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
	
	@Nullable public Inventory getInventory(Side s){
		return NoSuchInventory.INSTANCE;
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
}
