/**
 * 
 */
package mmb.engine.block;

import java.awt.Graphics;
import java.awt.Point;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.PropertyExtension;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.worlds.world.Player;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A basic building block with no data storage or tick support.
 * Use this for simple building blocks
 * Use {@link BlockEntity} and {@link BlockType} for more advanced blocks
 * @author Monniasza
 */
public class Block extends BlockType implements BlockEntry{
	
	/**
	 * Creates a new basic building block type
	 * @param id block ID
	 * @param properties additional properties
	 */
	public Block(String id, PropertyExtension... properties) {
		super(id, properties);
		setBlockFactory(json -> this);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Block " + title() + "(" + id() + ")";
	}
	@Override
	public @NN JsonNode save() {
		return new TextNode(id());
	}
	@Override
	public BlockType itemType() {
		return this;
	}
	
	@Override
	public BlockEntry place(int x, int y, World map) {
		return map.place(this, x, y);
	}

	//Placement GUI
	@Override
	public void openGUI(WorldWindow window) {
		//no GUI available
	}
	@Override
	public void closeGUI(WorldWindow window) {
		//no GUI available
	}
	
	//Placer preview
	@Override
	public void preview(Graphics g, Point renderStartPos, World map, Point targetLocation, int side) {
		getTexture().draw(this, renderStartPos, g, side);
	}
	
	//Surface block options
	private boolean surface;
	@Override
	public boolean isSurface() {
		return surface;
	}
	/**
	 * Sets whether this block is treated as 'surface block', thus is not mineable in 2D top down world in survival mode and machines.
	 * @param sf should this block be surface?
	 */
	public void setSurface(boolean sf) {
		surface = sf;
		setCollidable(!sf);
	}
	
	//Collidability
	private boolean collidable = true;
	/**
	 * @return is block collidable?
	 */
	public boolean isCollidable() {
		return collidable;
	}
	/**
	 * @param collidable the collidable to set
	 */
	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}
	@Override
	public void onPlayerCollide(int blockX, int blockY, World world, Player player) {
		if(collidable) BlockEntry.super.onPlayerCollide(blockX, blockY, world, player);
	}
	
	@Override
	public final BlockEntry blockCopy() {
		return this;
	}
	@Override
	public void resetMap(@Nil World map, int x, int y) {
		//does nothing
	}
	
	@Override
	public double getTypeDefaultVolume() {
		return 1.0 / 256;
	}
}
