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
	public SimpleItem getType();
	public String getName();
	public String getID();
}
