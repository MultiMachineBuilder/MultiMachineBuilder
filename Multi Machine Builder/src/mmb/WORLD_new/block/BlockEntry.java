/**
 * 
 */
package mmb.WORLD_new.block;

import java.awt.Point;
import java.util.List;

import mmb.WORLD_new.World;
import mmb.WORLD_new.block.properties.BlockProperty;

/**
 * @author oskar
 *
 */
public abstract class BlockEntry {
	//Positioning
	private final int x, y;
	public Point getPosition() {
		return new Point(x, y);
	}
	
	//Rendering
	public abstract BlockTexture getTexture();
	
	public BlockEntry(int x, int y, World owner) {
		super();
		this.x = x;
		this.y = y;
		this.owner = owner;
	}

	public World owner;
	
	//Block entity
	abstract public List<BlockProperty> getProperties();

	//Type
	abstract public BlockType getType();
	
	/**
	 * Get the original block
	 */
	abstract public BlockEntry original();
	
	public BlockEntry routeToOriginal() {
		int X = x;
		int Y = y;
		BlockEntry target = this;
		
		while(true) {
			target = original();
			if(target.x == X && target.y == Y) return target;
			X = target.x;
			Y = target.y;
		}
	}
	
	/**
	 * Get the offset from the original
	 */
	public Point getOffset() {
		Point orig = getOriginalPosition();
		return new Point(x - orig.x, y - orig.y);
	}
	
	/**
	 * Gets position of original block
	 */
	public Point getOriginalPosition() {
		return original().getPosition();
	}
}
