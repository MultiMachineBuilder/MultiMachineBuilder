/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.WORLD.item.Item;
import mmb.WORLD.items.Items;
import mmb.WORLD.worlds.map.BlockMap;

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
	public void place(int x, int y, BlockMap map) {
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
		return null;
	}
	@Override
	public BlockEntityType asBlockEntityType() {
		return null;
	}

	public void register(String id) {
		this.id = id;
		Blocks.register(this);
	}
	public void register() {
		Blocks.register(this);
	}
}
