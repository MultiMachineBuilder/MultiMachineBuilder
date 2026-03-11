/**
 * 
 */
package mmb.engine.block;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Objects;

import javax.swing.Icon;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.PropertyExtension;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.beans.IDrop;
import mmb.engine.chance.Chance;
import mmb.engine.item.Item;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A block type. Defines how blocks work and how they are saved.
 * @author oskar
 */
public class BlockType extends Item implements Placer, IDrop {
	private BlockFactory blockFactory;
	
	/**
	 * Sets this block type's block factory
	 * @param blockFactory new block factory
	 * @throws NullPointerException when blockFactory is null
	 */
	public void setBlockFactory(BlockFactory blockFactory) {
		Objects.requireNonNull(blockFactory, "blockFactory is null");
		this.blockFactory = blockFactory;
	}
	/**
	 * Creates or loads a block
	 * @param json JSON data to load, null for a new block
	 * @return a new block
	 */
	public BlockEntry createBlock(@Nil JsonNode json) {
		if(blockFactory == null)
			throw new IllegalStateException("Block factory has not been set");
		return blockFactory.createBlock(json);
	}
	
	
	@FunctionalInterface public static interface BlockFactory{
		public BlockEntry createBlock(@Nil JsonNode json);
	}
	
	/**
	 * Creates a new block type
	 * @param id the unique ID
	 * @param factory creates and loads block entries
	 * @param properties additional properties
	 */
	public BlockType(String id, PropertyExtension... properties) {
		super(id, properties);
	}

	@Override
	public void preview(Graphics g, Point renderStartPos, World map, Point targetLocation, int side) {
		getTexture().draw(null, renderStartPos, g, side);
	}
	
	private BlockType leaveBehind;
	
	public boolean isSurface = false;

	@Override
	public void openGUI(WorldWindow window) {
		//unused
	}

	@Override
	public void closeGUI(WorldWindow window) {
		//unused
	}

	public void setLeaveBehind(BlockType block) {
		Objects.requireNonNull(block, "leave behind is null");
		this.leaveBehind = block;
	}
	public BlockType leaveBehind() {
		BlockType leave0 = leaveBehind;
		if(leave0 == null) {
			leaveBehind = Blocks.grass;
			return Blocks.grass;
		}
		return leave0;
	}
	
	@Override
	public Icon getIcon() {
		return icon();
	}
	@Override
	public BlockEntry place(int x, int y, World that) {
		return that.place(this, x, y);
	}
	
	private Chance drop = this;
	@Override
	public @NN Chance drop() {
		return drop;
	}
	@Override
	public void setDrop(Chance drop) {
		Objects.requireNonNull(drop, "drop is null");
		this.drop = drop;
	}
	@Override
	public double getTypeDefaultVolume() {
		return 1.0 / 64;
	}
}
