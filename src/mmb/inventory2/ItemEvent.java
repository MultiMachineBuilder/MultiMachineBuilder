package mmb.inventory2;

import java.util.Objects;

import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;
import mmb.fluid.FluidEventType;

/**
 * Represents an addition, insertion or extraction of an item
 */
public record ItemEvent(@NN ItemEntry item, int before, int after) {
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
