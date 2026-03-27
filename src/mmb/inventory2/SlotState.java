package mmb.inventory2;

import mmb.annotations.Nil;
import mmb.engine.Verify;
import mmb.engine.item.ItemEntry;

/**
 * Represents contents of an item slot at an instant.
 * Also verifies the contents to make sure they're valid.
 */
public record SlotState(double maxSlotVolume, @Nil ItemEntry item, int quantity, boolean locked) {
	/**
	 * Creates a slot state
	 * @param maxSlotVolume maximum volume of items in this slot
	 * @param item item stored in this slot or locked to
	 * @param quantity quantity of an item
	 * @param locked is the slot locked?
	 * @throws IllegalArgumentException when item == null && quantity != 0
	 * @throws IllegalArgumentException when locked && item == null
	 * @throws IllegalArgumentException when quantity is not non-negative (less than 0, NaN or infinite)
	 * @throws IllegalArgumentException when capacity is not positive
	 */
	public SlotState {
		if(item == null && locked) throw new IllegalArgumentException("Item slots must not be locked to null");
		if(item == null && quantity > 0) throw new IllegalArgumentException("Item slots with no item must not have a quantity");
		Verify.requirePositive(maxSlotVolume);
	}
}
