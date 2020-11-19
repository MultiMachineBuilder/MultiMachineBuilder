/**
 * 
 */
package mmb.WORLD.tileworld.block;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import mmb.WORLD.inventory.ItemType;
import mmb.WORLD.inventory.SimpleItem;
import mmb.WORLD.tileworld.BlockDrawer;
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
		return "Block [name=" + name + ", leaveBehind=" + leaveBehind.name + "]";
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

}
