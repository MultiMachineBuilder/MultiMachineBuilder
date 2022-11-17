/**
 * 
 */
package mmb.world.modulars;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.data.variables.Variable;
import mmb.debug.Debugger;
import mmb.world.inventory.storage.BaseSingleItemInventory;
import mmb.world.item.ItemEntry;
import mmb.world.modulars.BlockModule.BlockModuleParams;
import mmb.world.rotate.Side;
import mmb.world.worlds.world.World;

/**
 * A type-checked slot for a module.
 * The class should be extended to add requested functionality.
 * For best results, use {@link ModuleSlot} for modules, or {@link CoreSlot} for the core
 * @author oskar
 * @param <T> type of values
 */
public class Slot<T> implements Variable<@Nullable T>{
	@Nonnull private static final Debugger debug = new Debugger("MODULAR SLOTS");
	/** The type of the slots */
	@Nonnull public final Class<T> type;
	@Nullable private T value;
	@Override
	public final T get() {
		return value;
	}
	@Override
	public void set(@Nullable T value) {
		removeOld(this.value);
		addNew(value);
		this.value = value;
	}
	/**
	 * Sets a value if it extends the expected type
	 * @param value
	 * @return does the input have a correct type
	 */
	@SuppressWarnings("unchecked")
	public boolean setto(@Nullable Object value) {
		if(!test(value)) return false;
		this.value = (T) value;
		return true;
	}
	/**
	 * Tests the type of the input
	 * @param totest value to test
	 * @return does the input have a correct type
	 */
	public final boolean test(@Nullable Object totest) {
		return totest == null || type.isInstance(totest);
	}
	/**
	 * Creates a slot
	 * @param type type of values;
	 */
	public Slot(Class<T> type) {
		this.type = type;
	}
	/**
	 * Called when an old value is removed
	 * @param module removed module
	 */
	protected void removeOld(@Nullable T module) {
		//to be overridden
	}
	/**
	 * Called when a new value is added
	 * @param module
	 */
	protected void addNew(@Nullable T module) {
		//to be overridden
	}
	
	/**
	 * An implementation of a slot for block modules
	 * @author oskar
	 * @param <Tblock> type of the hosting block
	 * @param <Tmodule> type of the block module
	 */
	public static class ModuleSlot<Tblock extends ModularBlock<Tblock, Tmodule, ?, ?>, Tmodule extends BlockModule<Tmodule>> extends Slot<Tmodule>{
		/** The logical side on which the slot is located */
		@Nonnull public final Side logicalSide;
		/** The owner of this slot */
		@Nonnull public final Tblock block;
		/**
		 * Creates a module slot
		 * @param logicalSide the logical side (before rotation)
		 * @param block block which hosts this slot
		 * @param type type of values
		 */
		public ModuleSlot(Side logicalSide, Tblock block, Class<Tmodule> type) {
			super(type);
			this.block = block;
			this.logicalSide = logicalSide;
		}

		@Override
		protected void removeOld(@Nullable Tmodule module) {
			if(module == null) return;
			//When killed, drop items
			//Find where to drop
			World w = block.owner();
			int x = block.posX();
			int y = block.posY();
			//Drop the items
			debug.printl("Dropping items");
			Side realSide = block.getChirotation().apply(logicalSide);
			BlockModuleParams<Tmodule> bmp = new BlockModuleParams<>(block, logicalSide, realSide);
			w.dropChance(module.dropItems(), x, y);
			module.onBroken(bmp);
		}

		@Override
		protected void addNew(@Nullable Tmodule module) {
			Side realSide = block.getChirotation().apply(logicalSide);
			BlockModuleParams<Tmodule> bmp = new BlockModuleParams<>(block, logicalSide, realSide);
			if(module != null) module.onAdded(bmp);
		}
	}
	/**
	 * An implementation of a slot for block modules
	 * @author oskar
	 * @param <Tblock> type of the hosting block
	 * @param <Tcore> type of the block core
	 */
	public static class CoreSlot<Tblock extends ModularBlock<Tblock, ?, Tcore, ?>, Tcore extends BlockCore<Tcore>> extends Slot<Tcore>{
		/** The owner of this slot */
		@Nonnull public final Tblock block;
		/**
		 * Creates a core slot
		 * @param block block which hosts this slot
		 * @param type type of values
		 */
		public CoreSlot(Tblock block, Class<Tcore> type) {
			super(type);
			this.block = block;
		}

		@Override
		protected void removeOld(@Nullable Tcore module) {
			if(module == null) return;
			//When killed, drop items
			//Find where to drop
			World w = block.owner();
			int x = block.posX();
			int y = block.posY();
			//Drop the items
			debug.printl("Dropping items");
			w.dropChance(module.dropItems(), x, y);
			module.onBroken(block);
		}

		@Override
		protected void addNew(@Nullable Tcore module) {
			if(module != null) module.onAdded(block);
		}
	}
	/**
	 * An implementation for the {@link ModularBlock#slotInternal(Side)} method with slots on all sides
	 * @author oskar
	 * @param <Tblock> type of the hosting block
	 * @param <Tmodule> type of the block modules
	 */
	public static class SidedSlotHelper<Tblock extends ModularBlock<Tblock, Tmodule, ?, ?>, Tmodule extends BlockModule<Tmodule>>{
		/** The upper slot */
		@Nonnull public final ModuleSlot<Tblock, Tmodule> U;
		/** The lower slot */
		@Nonnull public final ModuleSlot<Tblock, Tmodule> D;
		/** The left slot */
		@Nonnull public final ModuleSlot<Tblock, Tmodule> L;
		/** The right slot */
		@Nonnull public final ModuleSlot<Tblock, Tmodule> R;
		/**
		 * Creates a sided slot helper
		 * @param cls type of the modules
		 * @param block host block
		 */
		public SidedSlotHelper(Class<Tmodule> cls, Tblock block) {
			U = new ModuleSlot<>(Side.U, block, cls);
			D = new ModuleSlot<>(Side.D, block, cls);
			L = new ModuleSlot<>(Side.L, block, cls);
			R = new ModuleSlot<>(Side.R, block, cls);
		}
		/**
		 * Gets a slot on a given side
		 * @param s side to get from
		 * @return a module slot, or null if side is a corner or null
		 */
		@Nullable public ModuleSlot<Tblock, Tmodule> get(Side s){
			switch(s) {
			case U: return U;
			case D: return D;
			case L: return L;
			case R: return R;
			default: return null;
			}
		}
	}
	/**
	 * An inventory implementation using a slot
	 * @author oskar
	 * @param <Titem> type of items in the slot
	 */
	public static class SlotInv<Titem extends ItemEntry> extends BaseSingleItemInventory{
		/** The slot used by the inventory */
		@Nonnull public final Slot<Titem> slot;
		/**
		 * Creates a slot-based inventory
		 * @param slot slot to use
		 */
		public SlotInv(Slot<Titem> slot) {
			this.slot = slot;
			setCapacity(Double.POSITIVE_INFINITY);
		}
		@Override
		public ItemEntry getContents() {
			return slot.value;
		}
		@Override
		public boolean setContents(@Nullable ItemEntry contents) {
			return slot.setto(contents);
		}
		
	}
}