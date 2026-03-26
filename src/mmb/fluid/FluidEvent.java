package mmb.fluid;

import java.util.Objects;

import mmb.annotations.Nil;

/**
 * Represents a change in contents of a fluid handler.
 * If a fluid is added, {@code oldState} will be null.
 * If a fluid is removed, {@code newState} will be null.
 * Note that both {@code oldState} and {@code newState} mustn't be null together, since what would it mean?
 * The events are of the types:
 * <ul>
 *   <li>addition - oldState == null && newState != null</li>
 *   <li>removal - newState == null && oldState != null</li>
 *   <li>replacement - oldState != null && newState != null && oldState.fluid() != newState.fluid()</li>
 *   <li>mofification - oldState != null && newState != null && oldState.fluid() == newState.fluid()</li>
 * </ul>
 */
public record FluidEvent(@Nil FluidState oldState, @Nil FluidState newState, int index) {
	/**
	 * Constructs a new fluid event
	 * @param oldState old contents of a fluid tank
	 * @param newState new contents of a fluid tank
	 * @param index index of element changed
	 * @throws IllegalArgumentException when oldState == null && newState == null
	 * @throws IndexOutOfBoundsException when index < 0
	 */
	public FluidEvent{
		if(oldState == null && newState == null) throw new IllegalArgumentException("Both old and new can't be null together");
		if(index < 0) throw new IndexOutOfBoundsException("index < 0");
	}
	
	/**
	 * Classifies this event based on how it changes the fluid layout
	 * @return classification of this fluid event
	 */
	public FluidEventType classify() {		
		if(oldState == null) return FluidEventType.ADD;
		if(newState == null) return FluidEventType.REMOVE;
		if(Objects.equals(oldState.fluid(), newState.fluid())) return FluidEventType.MODIFY;
		return FluidEventType.REPLACE;
	}
}
