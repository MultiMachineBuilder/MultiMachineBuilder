/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

/**
 * @author oskar
 *
 */
public class DestructibleInventory implements Inventory{
	private Inventory inv;
	private DestructibleInventory(Inventory inv) {
		super();
		this.inv = inv;
	}
	/**
	 * @param entry
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#get(mmb.WORLD.inventory.ItemEntry)
	 */
	@Override
	public ItemRecord get(ItemEntry entry) {
		return inv.get(entry);
	}
	/**
	 * @param ent
	 * @param amount
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#insert(mmb.WORLD.inventory.ItemEntry, int)
	 */
	@Override
	public int insert(ItemEntry ent, int amount) {
		return inv.insert(ent, amount);
	}
	/**
	 * @param ent
	 * @param amount
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#extract(mmb.WORLD.inventory.ItemEntry, int)
	 */
	@Override
	public int extract(ItemEntry ent, int amount) {
		return inv.extract(ent, amount);
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#capacity()
	 */
	@Override
	public double capacity() {
		return inv.capacity();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#volume()
	 */
	@Override
	public double volume() {
		return inv.volume();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#exists()
	 */
	@Override
	public boolean exists() {
		return inv.exists();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#canExtract()
	 */
	@Override
	public boolean canExtract() {
		return inv.canExtract();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#canInsert()
	 */
	@Override
	public boolean canInsert() {
		return inv.canInsert();
	}
	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ItemRecord> iterator() {
		return inv.iterator();
	}
	
	public void destroy() {
		inv = NoSuchInventory.INSTANCE;
	}
}
