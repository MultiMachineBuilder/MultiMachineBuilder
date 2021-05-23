/**
 * 
 */
package mmb.WORLD.gui.inv;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.annotation.Nonnull;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.WORLD.gui.Variable;
import mmb.WORLD.inventory.ItemEntry;
import mmb.debug.Debugger;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class CraftingGrid extends JPanel{
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
		Border border = new BevelBorder(BevelBorder.LOWERED);
		this.size = size;
		setLayout(new GridLayout(size, size));
		slots = new ItemSelectionSlot[size][size];
		Dimension dim = new Dimension(40, 40);
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++){
				ItemSelectionSlot slot = new ItemSelectionSlot();
				slots[i][j] = slot;
				slot.setBorder(border);
				slot.setMinimumSize(dim);
				slot.setMaximumSize(dim);
				slot.setPreferredSize(dim);
				int x = i;
				int y = j;
				slot.stateChanged.addListener(item -> gridStateChanged.trigger(new ItemGridStateChangedEvent(x, y, item)));
				add(slot);
			}
		}
		
		int layoutSize = size * 40;
		Dimension largerDim = new Dimension(layoutSize, layoutSize);
		setMinimumSize(largerDim);
		setMaximumSize(largerDim);
		setPreferredSize(largerDim);
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

	private static final Debugger debug = new Debugger("CRAFTING GRID");
	/**
	 * Invoked when any item in the grid changes
	 */
	public final Event<ItemGridStateChangedEvent> gridStateChanged
	= new CatchingEvent<>(debug, "Failed to run grid state changed event");
	/**
	 * @author oskar
	 * Represents the change in selected item in this grid.
	 */
	public final class ItemGridStateChangedEvent{
		public final int x;
		public final int y;
		public final ItemEntry newEntry;
		public ItemGridStateChangedEvent(int x, int y, ItemEntry newEntry) {
			super();
			this.x = x;
			this.y = y;
			this.newEntry = newEntry;
		}
		@Override
		public String toString() {
			return "ItemGridStateChangedEvent [x=" + x + ", y=" + y + ", newEntry=" + newEntry + "]";
		}
	}
	@Nonnull public final Grid<ItemEntry> items = new Grid<ItemEntry>() {

		@Override
		public void set(int x, int y, ItemEntry data) {
			slots[x][y].setSelection(data);
		}

		@Override
		public ItemEntry get(int x, int y) {
			return slots[x][y].getSelection();
		}

		@Override
		public int width() {
			return size;
		}

		@Override
		public int height() {
			return size;
		}
		
	};
}
