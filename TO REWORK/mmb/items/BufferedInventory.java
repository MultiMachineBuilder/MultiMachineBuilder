/**
 *
 */
package mmb.items;

import java.io.IOException;

/**
 * @author oskar
 *
 */
public class BufferedInventory implements Inventory {
	int index;
	public BufferedInventory() {

	}

	@Override
	public boolean isVolatile() {
		return false;
	}

	@Override
	public Item insert(Item input) {
		return null;
	}

	@Override
	public ItemStack insert(ItemStack input) {
		return null;
	}

	@Override
	public ItemStack extract(Item target, int amount) {
		return null;
	}

	@Override
	public ItemStack extract(int slot, int amount) {
		return null;
	}

	@Override
	public ItemStack[] contents() {
		return new ItemStack[0];
	}

	@Override
	public void close() throws IOException {

	}
}
