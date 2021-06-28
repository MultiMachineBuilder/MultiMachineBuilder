/**
 * 
 */
package mmb.WORLD.item;

import javax.swing.Icon;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Titled;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.texture.BlockDrawer;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface ItemType extends Titled, Identifiable<String>{
	public double volume();
	public void setVolume(double volume);
	
	public void setDescription(String description);
	public String description();
	
	public void setID(String id);
	//id()
	
	public void setTitle(String title);
	//title()
	
	public void setTexture(BlockDrawer texture);
	public BlockDrawer getTexture();
	public default Icon getIcon() {
		return getTexture().toIcon();
	}
	
	/**
	 * @return a new instance of the item
	 */
	public ItemEntry create();
	public ItemEntry load(JsonNode node);
	
	default public boolean exists() {
		return true;
	}

	
}
