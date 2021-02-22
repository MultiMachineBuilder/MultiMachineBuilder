/**
 * 
 */
package mmb.WORLD.item;

import mmb.BEANS.Identifiable;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.items.Items;

/**
 * @author oskar
 *
 */
public class Item implements Identifiable<String>, ItemType {
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
	public boolean equals(Object other) {
		if(this == other) return true;
		if(other == null) return false;
		if(!(other instanceof Identifiable)) return false;
		Object id1 = ((Identifiable<?>)other).id();
		if(id == id1) return true;
		if(id1 == null) return false;
		return id1.equals(id);
	}

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
	public double volume;
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
}
