/**
 * 
 */
package mmb.WORLD.gui.inv;

import java.awt.GridLayout;

import javax.swing.JPanel;

import mmb.WORLD.gui.Variable;
import mmb.WORLD.inventory.ItemEntry;

/**
 * @author oskar
 *
 */
public class CraftingGrid extends JPanel {
	private static final long serialVersionUID = 7986725815517020684L;
	
	/**
	 * Size of this grid
	 */
	public final int size;
	/**
	 * A grid of all of the slots
	 */
	public final ItemSelectionSlot[][] slots;
	
	/**
	 * Creates a new crafting grid
	 * @param size grid size
	 */
	public CraftingGrid(int size) {
		this.size = size;
		setLayout(new GridLayout(size, size));
		slots = new ItemSelectionSlot[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++){
				ItemSelectionSlot slot = new ItemSelectionSlot();
				slots[i][j] = slot;
				add(slot);
			}
		}
	}
	
	/**
	 * Set source for all of the slots
	 * @param src source
	 */
	public void setSource(Variable<ItemEntry> src) {
		for(ItemSelectionSlot[] slotss : slots) {
			for(ItemSelectionSlot slot : slotss) {
				slot.setSelectionSrc(src);
			}
		}
	}
	
	/**
	 * @return new grid, which can be filled with items
	 */
	public ItemEntry[][] generateEmptyItemsGrid(){
		return new ItemEntry[size][size];
	}
	/**
	 * @return new array, which can be filled with items
	 */
	public ItemEntry[] generateEmptyItemsArray(){
		return new ItemEntry[size*size];
	}
	
	/**
	 * @return new grid with items
	 */
	public ItemEntry[][] generateItemsGrid(){
		ItemEntry[][] data = generateEmptyItemsGrid();
		readData(data);
		return data;
	}
	/**
	 * @return new array with items
	 */
	public ItemEntry[] generateItemsArray(){
		ItemEntry[] data = generateEmptyItemsArray();
		readData(data);
		return data;
	}

	/**
	 * Read the item data into the grid
	 * @param data target grid
	 */
	public void readData(ItemEntry[][] data) {
		int i = 0;
		for(ItemSelectionSlot[] slotss : slots) {
			int j = 0;
			for(ItemSelectionSlot slot : slotss) {
				data[i][j] = slot.getSelection();
				j++;
			}
			i++;
		}
	}
	/**
	 * Read the item data into the array
	 * @param data target array
	 */
	public void readData(ItemEntry[] data) {
		int i = 0;
		for(ItemSelectionSlot[] slotss : slots) {
			for(ItemSelectionSlot slot : slotss) {
				data[i] = slot.getSelection();
				i++;
			}
		}
	}
}
