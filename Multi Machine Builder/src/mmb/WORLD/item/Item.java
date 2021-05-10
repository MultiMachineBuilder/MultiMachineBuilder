/**
 * 
 */
package mmb.WORLD.item;

import javax.annotation.Nullable;
import javax.swing.Icon;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.WORLD.block.Drop;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemEntry;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class Item implements ItemType, ItemEntry, Drop {
	private static final Debugger debug = new Debugger("ITEMS");
	/**
	 * Get the hash code, which is always the same as ID's hash code.
	 * @return item's hash code.
	 * @see java.lang.String#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	@Override
	public boolean equals(@Nullable Object other) {
		if(this == other) return true;
		if(other == null) return false;
		if(!(other instanceof Identifiable)) return false;
		Object id1 = ((Identifiable<?>)other).id();
		if(id == id1) return true;
		if(id1 == null) return false;
		return id1.equals(id);
	}

	/**
	 * Register this item
	 * @param id new identifier
	 */
	public void register(String id) {
		this.id = id;
		Items.register(this);
	}
	public void register() {
		Items.register(this);
	}
	
	//Volume
	/**
	 * The volume which item takes up
	 */
	public double volume = 0.02;
	@Override
	public double volume() {
		return volume;
	}
	@Override
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	//Description
	/** 
	 * A description contains extra information about the item, which can be used by their users.
	 */
	public String description;
	@Override
	public String description() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	//Texture
	/**
	 * The texture determines the block's or items's looks.
	 * <br> If texture is null, the block or item won't be drawn.
	 */
	public BlockDrawer drawer;
	@Override
	public void setTexture(BlockDrawer texture) {
		drawer = texture;
	}
	@Override
	public BlockDrawer getTexture() {
		return drawer;
	}
	
	//ID
	/**
	 * The identifier is an unique name used in code. If identifier is null, a {@link NullPointerException} is thrown.
	 */
	public String id;
	@Override
	public void setID(String id) {
		this.id = id;
	}
	@Override
	public String id() {
		return id;
	}
	
	//Title
	/**
	 * A title is name which is displayed in toolbars. If title is null, it will be set to the ID.
	 */
	public String title;
	@Override
	public String title() {
		return title;
	}
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public ItemEntry create() {
		return this;
	}
	@Override
	public ItemEntry itemClone() {
		return this;
	}
	@Override
	public ItemType type() {
		return this;
	}
	@Override
	public boolean exists() {
		return true;
	}
	@Override
	public Icon getIcon() {
		return drawer.toIcon();
	}
	@Override
	public ItemEntry load(JsonNode node) {
		debug.printl("Attempting to load a non-data item");
		return this;
	}
	@Override
	public boolean drop(@Nullable Inventory inv, BlockMap map, int x, int y) {
		if(inv == null || !inv.exists()) {
			//TODO Drop to world
			return true; //NYI
		}
		//Drop to inventory
		return inv.insert(this, 1) == 1;
	}
}
