/**
 * 
 */
package mmb.WORLD.item;

import mmb.BEANS.Identifiable;
import mmb.BEANS.Titled;
import mmb.WORLD.BlockDrawer;

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
	//identifier()
	
	public void setTitle(String title);
	//title()
	
	public void setTexture(BlockDrawer texture);
	public BlockDrawer getTexture();
	
}
