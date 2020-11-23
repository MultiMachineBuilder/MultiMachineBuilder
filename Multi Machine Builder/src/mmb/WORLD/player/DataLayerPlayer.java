/**
 * 
 */
package mmb.WORLD.player;

import com.google.gson.JsonObject;
import mmb.DATA.save.DataLayer;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.InventoryHandler;
import mmb.WORLD.inventory.InventoryUnstackable;
import mmb.WORLD.inventory.Item;
import mmb.WORLD.inventory.ItemJSONConverter;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.world.BlockProxy;

/**
 * @author oskar
 *
 */
public class DataLayerPlayer implements DataLayer, InventoryHandler {
	/**
	 * @param slot
	 * @param amount
	 * @return
	 * @see mmb.WORLD.inventory.InventoryHandler#withdraw(int, int)
	 */
	public ItemStack withdraw(int slot, int amount) {
		return inv.withdraw(slot, amount);
	}
	/**
	 * @param itm
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#withdraw(mmb.WORLD.inventory.Item)
	 */
	public boolean withdraw(Item itm) {
		return inv.withdraw(itm);
	}
	/**
	 * @param itm
	 * @param amount
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#withdraw(mmb.WORLD.inventory.Item, int)
	 */
	public int withdraw(Item itm, int amount) {
		return inv.withdraw(itm, amount);
	}
	/**
	 * @param slot
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#withdraw(int)
	 */
	public Item withdraw(int slot) {
		return inv.withdraw(slot);
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.InventoryHandler#getMaxSlots()
	 */
	public int getMaxSlots() {
		return inv.getMaxSlots();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.InventoryHandler#slotsInUse()
	 */
	public int slotsInUse() {
		return inv.slotsInUse();
	}
	/**
	 * @param itms
	 * @see mmb.WORLD.inventory.InventoryHandler#setContents(mmb.WORLD.inventory.Item[])
	 */
	public void setContents(Item[] itms) {
		inv.setContents(itms);
	}
	/**
	 * @param itms
	 * @see mmb.WORLD.inventory.InventoryHandler#setContents(mmb.WORLD.inventory.ItemStack[])
	 */
	public void setContents(ItemStack[] itms) {
		inv.setContents(itms);
	}
	/**
	 * @param capacity
	 * @see mmb.WORLD.inventory.InventoryHandler#ensureCapacity(int)
	 */
	public void ensureCapacity(int capacity) {
		inv.ensureCapacity(capacity);
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.InventoryHandler#isStackable()
	 */
	public boolean isStackable() {
		return inv.isStackable();
	}
	/**
	 * @param limit
	 * @see mmb.WORLD.inventory.InventoryHandler#setLimited(int)
	 */
	public void setLimited(int limit) {
		inv.setLimited(limit);
	}
	/**
	 * @param itm
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#insert(mmb.WORLD.inventory.Item)
	 */
	public boolean insert(Item itm) {
		return inv.insert(itm);
	}
	/**
	 * @param itm
	 * @param amount
	 * @return
	 * @see mmb.WORLD.inventory.InventoryHandler#insert(mmb.WORLD.inventory.Item, int)
	 */
	public int insert(Item itm, int amount) {
		return inv.insert(itm, amount);
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#getCapacity()
	 */
	public double getCapacity() {
		return inv.getCapacity();
	}
	/**
	 * @param capacity
	 * @see mmb.WORLD.inventory.Inventory#setCapacity(double)
	 */
	public void setCapacity(double capacity) {
		inv.setCapacity(capacity);
	}
	/**
	 * @param itms
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#insert(mmb.WORLD.inventory.ItemStack)
	 */
	public int insert(ItemStack itms) {
		return inv.insert(itms);
	}
	/**
	 * @param itms
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#moveFrom(mmb.WORLD.inventory.ItemStack)
	 */
	public boolean moveFrom(ItemStack itms) {
		return inv.moveFrom(itms);
	}
	/**
	 * @param itms
	 * @param amount
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#moveFrom(mmb.WORLD.inventory.ItemStack, int)
	 */
	public int moveFrom(ItemStack itms, int amount) {
		return inv.moveFrom(itms, amount);
	}
	/**
	 * @param itms
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#empty(mmb.WORLD.inventory.ItemStack)
	 */
	public int empty(ItemStack itms) {
		return inv.empty(itms);
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#getLimited()
	 */
	public boolean getLimited() {
		return inv.getLimited();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#leftoverVolume()
	 */
	public double leftoverVolume() {
		return inv.leftoverVolume();
	}
	/**
	 * 
	 * @see mmb.WORLD.inventory.Inventory#calcUsedVolume()
	 */
	public void calcUsedVolume() {
		inv.calcUsedVolume();
	}
	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return inv.equals(obj);
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#getContents()
	 */
	public ItemStack[] getContents() {
		return inv.getContents();
	}
	/**
	 * @return
	 * @see mmb.WORLD.inventory.Inventory#usedVolume()
	 */
	public double usedVolume() {
		return inv.usedVolume();
	}

	public Inventory inv;
	public boolean creative = true;
	
	public static DataLayerPlayer load(JsonObject jo) {
		DataLayerPlayer dlp = new DataLayerPlayer(ItemJSONConverter.entryToInventory(jo));
		if(jo.has("creative")) dlp.creative = jo.get("creative").getAsBoolean();
		return dlp;
	}
	public static DataLayerPlayer empty() {
		return new DataLayerPlayer();
	}
	public DataLayerPlayer(Inventory inv) {
		super();
		this.inv = inv;
	}
	/**
	 * 
	 */
	private DataLayerPlayer() {
		inv = new InventoryUnstackable();
		inv.capacity = 2;
		inv.setLimited(-1);
	}
	@Override
	public JsonObject save() {
		JsonObject jo = ItemJSONConverter.inventoryToEntry(inv);
		jo.addProperty("creative", creative);
		return jo;
	}

	@Override
	public String name() {
		return "PlayerInv";
	}
	@Override
	public boolean contains(Item itm) {
		return inv.contains(itm);
	}
	public BlockIcon newIcon(Block b) {
		if(creative) return new BlockIcon(b, pr);
		return new BlockIcon(b, inv, pr);
	}
	private BlockProxy pr;
	@Override
	public void setProxy(BlockProxy p) {
		pr = p;
	}

}
