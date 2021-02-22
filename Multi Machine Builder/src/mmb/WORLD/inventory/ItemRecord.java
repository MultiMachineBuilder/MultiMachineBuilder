/**
 * 
 */
package mmb.WORLD.inventory;

/**
 * @author oskar
 *
 */
public interface ItemRecord {
	public int getAmount();
	public int extract(int amount);
	public int insert(int amount);
	public ItemEntry getItem();
}
