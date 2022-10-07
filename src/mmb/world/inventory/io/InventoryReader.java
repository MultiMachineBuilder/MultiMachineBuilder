/**
 * 
 */
package mmb.world.inventory.io;

import javax.annotation.Nonnull;

import mmb.world.items.ItemEntry;

/**
 * @author oskar
 *
 */
public interface InventoryReader {
	/**
	 * An inventory reader without items
	 */
	@Nonnull InventoryReader NONE  = new InventoryReader() {

		@Override
		public int currentAmount() {
			return 0;
		}

		@Override
		public ItemEntry currentItem() {
			return null;
		}

		@Override
		public int extract(int amount) {
			return 0;
		}

		@Override
		public void skip() {
			//unused
		}

		@Override
		public int extract(ItemEntry entry, int amount) {
			return 0;
		}

		@Override
		public void next() {
			//unused
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public boolean hasCurrent() {
			return false;
		}

		@Override
		public ExtractionLevel level() {
			return ExtractionLevel.RANDOM;
		}
	};

	/**
	 * Gets current amount of items avaliable in current item record
	 * @return current number of items
	 */
	public int currentAmount();
	/**
	 * @implNote Call before extraction to check items
	 * @return the currently active 
	 */
	public ItemEntry currentItem();
	/**
	 * @param amount
	 * @return number of extracted items
	 */
	public int extract(int amount);
	/**
	 * Skips the queue
	 * @throws UnsupportedOperationException if this reader does not support skipping
	 */
	public void skip();

	/**
	 * Extracts current item
	 * @param entry item entry
	 * @param amount number of items to extract
	 * @return number of items extracted
	 * @throws UnsupportedOperationException if this reader does not support random access
	 */
	public int extract(ItemEntry entry, int amount);
	
	/**
	 * Goes to the next item record
	 * @throws IllegalStateException when there are still items under current entry
	 * @throws IllegalStateException when there are no more items
	 */
	public void next();
	/**
	 * @return are there any more items?
	 */
	public boolean hasNext();
	/**
	 * @implNote Example implementation:
	 * <br>If this returns true, extract items
	 * <br>Else if there are more items, go to the next item and continue from beginning
	 * <br>Else stop.
	 * @return are there any items here?
	 */
	public boolean hasCurrent();
	
	/**
	 * @author oskar
	 * Indicates the access level
	 */
	public enum ExtractionLevel{
		/** Indicates that items can only be extracted sequentially (like unskippable queues) */
		SEQUENTIAL(0),
		/** Indicates that items can be skipped (like skippable and circular queues) */
		SKIPPING(1), 
		/** Indicates that items can be extracted in any order */
		RANDOM(2);
		
		/**
		 * The ordinal extraction level
		 */
		public final int level;
		ExtractionLevel(int level){
			this.level = level;
		}
	}
	
	/**
	 * Gets the extraction level
	 * @return SEQUENTIAL - when only sequential access is allowed
	 * <br> SKIPPING - when skipping access is allowed
	 * <br> RANDOM - when random access is allowed
	 */
	public ExtractionLevel level();
	
	/**
	 * @implSpec The amount given by this function must be same as {@link #extract(int)} before calling it
	 * @param amount target number of items
	 * @return amount of items, which would be extracted by extract(int)
	 */
	default public int toBeExtracted(int amount) {
		return Math.min(amount, currentAmount());
	}
}
