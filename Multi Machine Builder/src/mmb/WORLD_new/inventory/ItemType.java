/**
 * 
 */
package mmb.WORLD_new.inventory;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ItemType {

	/**
	 * Get the hash code, which is always the same as ID's hash code.
	 * @return item's hash code.
	 * @see java.lang.String#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	//Naming
	/**
	 * The identifier is an unique name used in code. If identifier is null, a {@link NullPointerException} is thrown.
	 */
	public String id;
	/**
	 * A title is name which is displayed in toolbars. If title is null, it will be set to the ID.
	 */
	public String title;
	/**
	 * A description contains extra information about the item, which can be used by their users.
	 */
	public String description;
	
	//Texture
	/**
	 * The texture determines the block's or items's looks.
	 * <br> If texture is null, the block or item won't be drawn.
	 */
	public BlockDrawer drawer;
	
	public void register(String id) {
		this.id = id;
	}
}
