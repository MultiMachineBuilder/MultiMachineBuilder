/**
 * 
 */
package mmb.items;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import mmb.world.storage.LostProperty;

/**
 * @author oskar
 *
 */
public class VolatileInventory implements Inventory {
	/**
	 * If positive - it is max size of the inventory
	 * If negative - it is overhead on unlimited size inventory
	 */
	public int maxSize;
	
	/**
	 * Current number of slots in use. Not necessarily equal to amount of stored items.
	 */
	private int currentSize;
	
	/**
	 * Smallest size inventory can take
	 */
	private int minSize;
	
	public List<ItemStack> contents;
	
	/**
	 * 
	 * @param size see sizeLimit
	 * @return
	 */
	public static VolatileInventory sized(int size) {
		
	}
	
	/**
	 * Closes the inventory
	 * @throws IOException 
	 */
	public void finalize() throws IOException {
		close();
	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		for(int i = 0; i < contents.size(); i++) {
			LostProperty.LPO.insert(contents.get(i));
		}
	}

	/* (non-Javadoc)
	 * @see mmb.items.Inventory#isVolatile()
	 */
	@Override
	public boolean isVolatile() {
		return true;
	}

	/* (non-Javadoc)
	 * @see mmb.items.Inventory#insert(mmb.items.Item)
	 */
	@Override
	public Item insert(Item input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.items.Inventory#insert(mmb.items.ItemStack)
	 */
	@Override
	public ItemStack insert(ItemStack input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.items.Inventory#extract(mmb.items.Item, int)
	 */
	@Override
	public ItemStack extract(Item target, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.items.Inventory#extract(int, int)
	 */
	@Override
	public ItemStack extract(int slot, int amount) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
