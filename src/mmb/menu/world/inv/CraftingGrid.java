/**
 * 
 */
package mmb.menu.world.inv;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.pploder.events.Event;

import mmb.data.variables.ListenableValue;
import mmb.menu.components.ItemSelectionSlot;
import mmbeng.CatchingEvent;
import mmbeng.debug.Debugger;
import mmbeng.item.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 */
public class CraftingGrid extends JPanel implements AutoCloseable{
	private static final long serialVersionUID = 7986725815517020684L;
	
	//Grid data
	/** Size of this grid */
	public final int size;
	/** A grid of all of the slots */
	public final transient Grid<@Nonnull ItemSelectionSlot> slots;
	/** A grid of all item variables */
	public final transient Grid<@Nonnull ListenableValue<@Nullable ItemEntry>> listenvars;
	
	/**
	 * Creates a new crafting grid
	 * @param size grid size
	 */
	public CraftingGrid(int size) {
		Grid<@Nonnull ListenableValue<ItemEntry>> listenvars0 = new FixedGrid<>(size);
		listenvars = Collects.unmodifiableGrid(listenvars0);
		Grid<@Nonnull ItemSelectionSlot> slots0 = new FixedGrid<>(size);
		slots = Collects.unmodifiableGrid(slots0);
		
		Border border = new BevelBorder(BevelBorder.LOWERED);
		this.size = size;
		setLayout(new GridLayout(size, size));
		Dimension dim = new Dimension(40, 40);
		for(int yy = 0; yy < size; yy++) {
			for(int xx = 0; xx < size; xx++){
				@SuppressWarnings("resource") //closed later in close()
				ItemSelectionSlot slot = new ItemSelectionSlot();
				ListenableValue<@Nullable ItemEntry> variable = new ListenableValue<>(null);
				slots0.set(xx, yy, slot);
				listenvars0.set(xx, yy, variable);
				slot.setBorder(border);
				slot.setMinimumSize(dim);
				slot.setMaximumSize(dim);
				slot.setPreferredSize(dim);
				slot.setTarget(variable);
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
	public void setSource(Supplier<ItemEntry> src) {
		slots.forEach(slot -> slot.setSelector(src));
	}
	
	/**
	 * Read the item data into the grid
	 * @param data target grid
	 */
	@SuppressWarnings("resource")
	public void readData(ItemEntry[][] data) {
		for(int i = 0; i < slots.width(); i++) {
			for(int j = 0; j < slots.height(); j++) {
				slots.get(i, j).setSelection(data[i][j]);
			}
		}
	}

	@Nonnull private static final Debugger debug = new Debugger("CRAFTING GRID");
	/**
	 * Invoked when any item in the grid changes
	 */
	public final transient Event<ItemGridStateChangedEvent> gridStateChanged
	= new CatchingEvent<>(debug, "Failed to run grid state changed event");
	/**
	 * @author oskar
	 * Represents the change in selected item in this grid.
	 */
	public final class ItemGridStateChangedEvent{
		/** The X coordinate of a change */
		public final int x;
		/** The Y coordinate of a change */
		public final int y;
		/** The new item at given location*/
		public final ItemEntry newEntry;
		/**
		 * Creates a new item grid state changed event
		 * @param x X coordinate of a change
		 * @param y Y coordinate of a change
		 * @param newEntry the new item at given location
		 */
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
	/** A grid of items in this crafting grid */
	@Nonnull public final transient Grid<@Nullable ItemEntry> items = new Grid<>() {
		@SuppressWarnings("resource")
		@Override
		public void set(int x, int y, @Nullable ItemEntry data) {
			slots.get(x, y).setSelection(data);
		}

		@SuppressWarnings("resource")
		@Override
		public ItemEntry get(int x, int y) {
			return slots.get(x, y).getSelection();
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
	
	@Override
	public void close(){
		slots.forEach(ItemSelectionSlot::close);
	}
}
