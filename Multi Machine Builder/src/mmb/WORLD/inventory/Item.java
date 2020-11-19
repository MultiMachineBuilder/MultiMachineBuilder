/**
 * 
 */
package mmb.WORLD.inventory;

/**
 * @author oskar
 *
 */
public interface Item {
	public double getVolume();
	public ItemType getType();
	public String getName();
	public String getID();
}
