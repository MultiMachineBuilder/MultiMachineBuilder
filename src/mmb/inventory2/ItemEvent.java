package mmb.inventory2;

import java.util.Objects;

import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;
import mmb.fluid.FluidEventType;

/**
 * Represents an addition, insertion or extraction of an item
 * @param inventory the handler whose logical contents changed
 * @param item the item entry whose stored amount changed
 * @param before the stored amount before the change, always {@code >= 0}
 * @param after the stored amount after the change, always {@code >= 0}
 */
public record ItemEvent(@NN ItemHandler inventory, @NN ItemEntry item, int before, int after) {
	/**
	 * Constructs a new item event
	 * @throws IllegalArgumentException when quantity < 0
	 * @throws NullPointerException when item == null
	 */
	public ItemEvent{
		Objects.requireNonNull(item, "item is null");
		if(before < 0) throw new IllegalArgumentException("before < 0");
		if(after < 0) throw new IllegalArgumentException("after < 0");
		if(before == 0 && after == 0) throw new IllegalArgumentException("before and after can't be both 0");
	}
	
	public FluidEventType classify() {
		if(before == 0) return FluidEventType.ADD;
		if(after == 0) return FluidEventType.REMOVE;
		return FluidEventType.MODIFY;
	}
}
