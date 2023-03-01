/**
 * 
 */
package mmb.engine.block;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.NN;
import mmb.Nil;
import mmb.engine.chance.Chance;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.worlds.world.Player;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A basic building block with no data storage or tick support.
 * Use this for simple building blocks
 * Use {@link BlockEntity} and {@link BlockEntityType} for more advanced blocks
 * @author oskar
 */
public class Block extends BlockBase implements BlockEntry{
	@Override
	public String toString() {
		return "Block " + title() + "(" + id() + ")";
	}
	@Override
	public @NN JsonNode save() {
		return new TextNode(id());
	}
	@Override
	public BlockType type() {
		return this;
	}
	
	@Override
	public BlockEntry place(int x, int y, World map) {
		return map.place(this, x, y);
	}
	@Override
	public BlockEntry createBlock() {
		return this;
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

	//Convienient setters
	@Override
	public Block texture(String texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public Block texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public Block texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	@Override
	public Block texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	@Override
	public Block title(String title) {
		setTitle(title);
		return this;
	}
	@Override
	public Block describe(String description) {
		setDescription(description);
		return this;
	}
	@Override
	public Block drop(Chance drop) {
		setDrop(drop);
		return this;
	}
	@Override
	public Block finish(String id) {
		register(id);
		return this;
	}
	@Override
	public Block leaveBehind(BlockType block) {
		setLeaveBehind(block);
		return this;
	}
	@Override
	@NN public Block volumed(double volume) {
		setVolume(volume);
		return this;
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
}
