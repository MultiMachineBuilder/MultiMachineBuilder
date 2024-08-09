/**
 * 
 */
package mmb.engine.inv.io;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import mmb.NN;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeOutput;

/**
 * An abstraction over extraction of items
 * @author oskar
 */
public interface InventoryReader {
	/** An inventory reader without items */
	@NN InventoryReader NONE = new InventoryReader() {
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
		public ExtractionLevel level() {
			return ExtractionLevel.RANDOM;
		}

		
		@Override
		public int toBeExtracted(ItemEntry item, int amount) {
			return 0;
		}

		@Override
		public int toBeExtractedBulk(RecipeOutput item, int amount) {
			return 0;
		}

		@Override
		public int toBeExtracted(int amount) {
			return 0;
		}

		@Override
		public int extractBulk(RecipeOutput block, int amount) {
			return 0;
		}
	};

	//Extraction
	/**
	 * Extracts items
	 * @param amount number of items to extract
	 * @return number of extracted items
	 */
	public int extract(int amount);
	/**
	 * Extracts current item
	 * @param entry item entry
	 * @param amount number of items to extract
	 * @return number of items extracted
	 * @throws UnsupportedOperationException if this reader does not support random access
	 */
	public int extract(ItemEntry entry, int amount);	
	/**
	 * Extracts units from an inventory, without breaking up the units
	 * The inventory may impose restrictions on the extraction, so users should check beforehand
	 * @param block extraction unit
	 * @param amount number of units to extract
	 * @throws IllegalArgumentException when negative units are extracted.
	 * @return number of units extracted
	 * @implNote The implementation is suitable for most inventories.
	 * If inventory restricts extractions beyond blocking them all, the {@link #toBeExtractedBulk(RecipeOutput, int)} should be overridden
	 */
	public int extractBulk(RecipeOutput block, int amount);
	
	//Iteration methods
	/**
	 * Goes to the next item record
	 * @throws IllegalStateException when there are still items under current entry (if inventory does not support skipping)
	 * @throws IllegalStateException when there are no more items
	 */
	public void next();
	/** @return are there any more items? */
	public boolean hasNext();
	/**
	 * Gets current amount of items avaliable in current item record
	 * @return current number of items
	 * @throws NoSuchElementException before calling next()
	 */
	public int currentAmount();
	/**
	 * @implNote Call before extraction to check items
	 * @return the currently active item
	 * @throws NoSuchElementException before calling next()
	 */
	@NN public ItemEntry currentItem();
	
	//Extracteration - temporary	
	/**
	 * An extraction opportunity.
	 * Used in extracterators.
	 * @apiNote These extraction opportunities become invalid after getting a next one.
	 * Use permanent extraction opportunities if later access is needed
	 * @author oskar
	 */
	public static class ExtractOpportunity{
		/** The item under this extraction opportunity */
		@NN public final ItemEntry item;
		/** The current inventory reader */
		@NN public final InventoryReader ir;
		/**
		 * Creates a new extraction opportunity
		 * @param item the item under this extraction opportunity
		 * @param ir the current inventory reader
		 */
		public ExtractOpportunity(ItemEntry item, InventoryReader ir) {
			this.item = item;
			this.ir = ir;
		}
		/**
		 * Extracts items like the inventory reader
		 * @param amount number of items to extract
		 * @return number of extracted items
		 */
		public int extract(int amount) {
			return ir.extract(amount);
		}
		/**
		 * Extracts items like the inventory reader
		 * @param amount number of items to extract
		 * @return number of extracted items
		 */
		public int toextract(int amount) {
			return ir.toBeExtracted(amount);
		}
	}
	/**
	 * Creates an extraction opportunity (temporary)
	 * @apiNote The extraction opportunity becomes invalid after getting a next one.
	 * @return a new extraction opportunity
	 */
	public default ExtractOpportunity createOpportunity() {
		ItemEntry item = currentItem();
		if(item == null) throw new IllegalStateException("No items");
		return new ExtractOpportunity(item, this);
	}
	/**
	 * Creates an extracterator
	 * (an iterator over the extraction opportunities)
	 * @return an extracterator
	 */
	public default Iterator<ExtractOpportunity> extracterator(){
		return extracterator0(this::createOpportunity);
	}
	/**
	 * Shared logic for extracterators
	 * @param <T> type of extracterators
	 * @param sup extraction opportunity factory
	 * @return an extracterator
	 */
	public default <T>Iterator<T> extracterator0(Supplier<T> sup){
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				return InventoryReader.this.hasNext();
			}

			@Override
			public T next() {
				InventoryReader.this.next();
				return sup.get();
			}
		};
	}
	
	//Extracteration - permanent
	/**
	 * A permanent extraction opportunity.
	 * Remains valid after getting a next one, and after writing to an inventory
	 * @author oskar
	 */
	public static class PermaExtractOpportunity extends ExtractOpportunity {
		/**
		 * Creates a new extraction opportunity
		 * @param item the item under this extraction opportunity
		 * @param ir the current inventory reader
		 */
		public PermaExtractOpportunity(ItemEntry item, InventoryReader ir) {
			super(item, ir);
		}
		@Override
		public int extract(int amount) {
			return ir.extract(item, amount);
		}

		@Override
		public int toextract(int amount) {
			return ir.toBeExtracted(item, amount);
		}
	}
	/**
	 * Creates a permanent extraction opportunity 
	 * @apiNote The extraction opportunity remains valid after getting a next one, and after writing to an inventory
	 * @return a new extraction opportunity
	 */
	public default PermaExtractOpportunity createOpportunityPerma() {
		ItemEntry item = currentItem();
		if(item == null) throw new IllegalStateException("No items");
		return new PermaExtractOpportunity(item, this);
	}
	/**
	 * Creates a permanent extracterator.
	 * The extraction opportunities are permanent
	 * (an iterator over the extraction opportunities)
	 * @apiNote The extraction opportunities remain valid after iteration, and after writing to an inventory
	 * @return an extracterator
	 */
	public default Iterator<PermaExtractOpportunity> permaExtracterator(){
		return extracterator0(this::createOpportunityPerma);
	}
	
	//Calculation
	/**
	 * Defines capabilities of this inventory reader
	 * @author oskar
	 */
	public enum ExtractionLevel{
		/** Indicates that items can only be extracted sequentially (like unskippable queues) */
		SEQUENTIAL(0),
		/** Indicates that items can be skipped (like skippable and circular queues) */
		SKIPPING(1), 
		/** Indicates that items can be extracted in any order */
		RANDOM(2);
		
		/** The ordinal extraction level */
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
	 * Amount of items which can be extracted
	 * @implSpec The amount given by this function must be same as {@link #extract(int)} before calling it
	 * @param amount target number of items
	 * @return amount of items, which would be extracted by extract(int)
	 */
	public default int toBeExtracted(int amount) {
		return Math.min(amount, currentAmount());
	}
	/**
	 * Amount of items which can be extracted
	 * @param item item
	 * @param amount target number of items
	 * @return amount of items, which would be extracted by extract(int)
	 * @throws UnsupportedOperationException if this reader does not support random access
	 */
	public int toBeExtracted(ItemEntry item, int amount);
	/**
	 * Amount of extraction unit which can be extracted without splitting them
	 * @param item extraction unit
	 * @param amount target number of units
	 * @return amount of items, which would be extracted by extract(int)
	 * @throws UnsupportedOperationException if this reader does not support random access
	 */
	public int toBeExtractedBulk(RecipeOutput item, int amount);
}
