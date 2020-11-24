/**
 * 
 */
package mmb.WORLD.tileworld.block;

import java.awt.Graphics;
import java.awt.Point;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import mmb.WORLD.inventory.ItemType;
import mmb.WORLD.inventory.SimpleItem;
import mmb.WORLD.inventory.items.Items;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.world.BlockProxy;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Block implements MapEntry, ItemType{
	public BlockDrawer texture;
	private String name;
	public String title;
	public double volume = 0.02;
	private static final Debugger debug = new Debugger("BLOCKS");
	
	/**
	 * Optionally creates block entity. Set to null to disable block entity support
	 */
	public BlockEntityLoader bel = null;
	
	/**
	 * Optional handler run for every frame
	 */
	public Consumer<BlockUpdateEvent> update = null;
	
	public BiConsumer<BlockUpdateEvent, BlockProxy> activate = (bue, bp) -> {};
	
	public BiConsumer<BlockUpdateEvent, BlockEntityData> bedHandler = (bue, bed) -> {};
	
	/**
	 * A block which is left behind when mined.
	 */
	public Block leaveBehind;
	/**
	 * 
	 */
	public Block() {
		if(Blocks.isGeneratingFurther()) leaveBehind = Blocks.grass;
	}
	
	public void register(String name) {
		if(title == null) title = name;
		this.name = name;
		Blocks.blocks.put(name, this);
		debug.printl("Creating a block: "+name);
		Items.items.put(name, this);
	}

	/**
	 * @return the name
	 */
	@Override
	public String getID() {
		return name;
	}

	

	@Override
	public String toString() {
		return "Block [name=" + name + ", title=" + title + ", volume=" + volume + ", leaveBehind=" + leaveBehind.name + "]";
	}

	@Override
	public BlockDrawer getTexture() {
		return texture;
	}

	@Override
	public Block getType() {
		return this;
	}

	@Override
	public BlockEntityData getBlockData() {
		return null;
	}

	@Override
	public Block clone() {
		return this;
	}

	@Override
	public Block deepClone() {
		return this;
	}

	@Override
	public Block survivalClone() {
		return this;
	}
	
	public BlockEntity load(JsonObject object) {
		BlockEntityData bed = bel.loader.apply(object);
		return new BlockEntity(this, bed);
	}

	@Override
	public double getVolume() {
		return 0.02;
	}

	@Override
	public String getName() {
		return title;
	}
	
	/**
	 * Creates a ready-to-place map entry.
	 * @return block which can be placed
	 */
	public MapEntry newBlock() {
		if(bel == null) return this;
		return new BlockEntity(this, bel.blockEntityGen.get());
	}

	/**
	 * @param x
	 * @param y
	 * @param g
	 * @see mmb.WORLD.tileworld.BlockDrawer#draw(int, int, java.awt.Graphics)
	 */
	public void draw(int x, int y, Graphics g) {
		texture.draw(x, y, g);
	}

	/**
	 * @param p
	 * @param g
	 * @see mmb.WORLD.tileworld.BlockDrawer#draw(java.awt.Point, java.awt.Graphics)
	 */
	public void draw(Point p, Graphics g) {
		texture.draw(p, g);
	}

}
