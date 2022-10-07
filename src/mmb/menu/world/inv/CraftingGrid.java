/**
 * 
 */
package mmb.menu.world.inv;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.pploder.events.Event;

import mmb.CatchingEvent;
import mmb.data.variables.Variable;
import mmb.debug.Debugger;
import mmb.world.items.ItemEntry;
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
		for(int yy = 0; yy < size; yy++) {
			for(int xx = 0; xx < size; xx++){
				ItemSelectionSlot slot = new ItemSelectionSlot();
				slots[xx][yy] = slot;
				slot.setBorder(border);
				slot.setMinimumSize(dim);
				slot.setMaximumSize(dim);
				slot.setPreferredSize(dim);
				int x = xx;
				int y = yy;
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
		public ItemGridStateChangedEvent(int x, int y, @Nullable ItemEntry newEntry) {
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
	@Nonnull public final Grid<@Nullable ItemEntry> items = new Grid<@Nullable ItemEntry>() {

		@Override
		public void set(int x, int y, @Nullable ItemEntry data) {
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
