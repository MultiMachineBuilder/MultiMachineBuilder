/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class Block extends BlockBase implements BlockEntry{

	@Override
	public JsonNode save() {
		return new TextNode(id);
	}

	@Override
	public boolean isBlockEntity() {
		return false;
	}

	@Override
	public SkeletalBlockEntity asBlockEntity() {
		return null;
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

	@Override
	public void openGUI() {
		//no GUI available
	}

	@Override
	public void closeGUI() {
		//no GUI available
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}
}
