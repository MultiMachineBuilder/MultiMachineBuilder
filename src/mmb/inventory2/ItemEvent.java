package mmb.inventory2;

import java.util.Objects;

import mmb.annotations.Nil;
import mmb.fluid.FluidEventType;

public record ItemEvent(@Nil SlotState oldState, @Nil SlotState newState, int index) {
	/**
	 * Constructs a new fluid event
	 * @param oldState old contents of a fluid tank
	 * @param newState new contents of a fluid tank
	 * @param index index of element changed
	 * @throws IllegalArgumentException when oldState == null && newState == null
	 * @throws IndexOutOfBoundsException when index < 0
	 */
	public ItemEvent{
		if(oldState == null && newState == null) throw new IllegalArgumentException("Both old and new can't be null together");
		if(index < 0) throw new IndexOutOfBoundsException("index < 0");
	}
	
	public FluidEventType classify() {
		if(oldState == null) return FluidEventType.ADD;
		if(newState == null) return FluidEventType.REMOVE;
		if(Objects.equals(oldState.item(), newState.item())) return FluidEventType.MODIFY;
		return FluidEventType.REPLACE;
	}
}
