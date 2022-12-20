/**
 * 
 */
package mmb.engine.inv.io;

import java.util.function.Predicate;

import mmb.NN;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SingleItem;
import mmb.engine.item.ItemEntry;

/**
 * @author oskar
 * Pushes items into inventory
 */
public interface InventoryWriter {
	//Insertion
	/**
	 * Pushes given item entry to the given inventory
	 * @param ent itemto insert
	 * @param amount number of items
	 * @return number of items inserted into inventory
	 */
	public int insert(ItemEntry ent, int amount);
	/**
	 * @param stack item stack to insert
	 * @return number of items inserted into inventory
	 */
	public default int insert(SingleItem stack) {
		return insert(stack.item(), stack.amount());
	}
	/**
	 * Inserts items, keeping the blocks whole
	 * @param block the indivisible insertion unit
	 * @param amount number of units
	 * @return inserted number of units
	 */
	public int bulkInsert(RecipeOutput block, int amount);
	/**
	 * Inserts items, keeping the block whole
	 * @param block the indivisible insertion unit
	 * @return inserted number of units
	 */
	public default boolean bulkInsert(RecipeOutput block) {
		return bulkInsert(block, 1) == 1;
	}	

	//Testing methods
	/**
	 * Checks insertability of the items
	 * @param block the indivisible insertion unit
	 * @param amount number of insertion units
	 * @return how many units can be inserted
	 */
	public int toInsertBulk(RecipeOutput block, int amount);
	/**
	 * Checks insertability of the items
	 * @param item item to insert
	 * @param amount number of items to insert
	 * @return how many items can be inserted?
	 */
	public int toInsert(ItemEntry item, int amount);
	
	//Various modifications
	/** Represents an interface which does not allow input */
	@NN public static final InventoryWriter NONE = new InventoryWriter() {
		@Override
		public int insert(ItemEntry ent, int amount) {
			return 0;
		}
		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			return 0;
		}
		@Override
		public int toInsertBulk(RecipeOutput outItems, int amount) {
			return 0;
		}
		@Override
		public int toInsert(ItemEntry item, int amount) {
			return 0;
		}
	};
	/**
	 * Writes items to the first writer, only if items match the filter.
	 * Otherwise it writes them to the second writer.
	 * @author oskar
	 */
	public static class Shunting implements InventoryWriter{
		private final InventoryWriter ifTrue;
		private final InventoryWriter ifFalse;
		private final Predicate<ItemEntry> filter;
		/**
		 * Creates a shunting writer
		 * @param ifTrue items go here if filter accepts them
		 * @param ifFalse items go here if filter rejects them
		 * @param filter item filter
		 */
		public Shunting(InventoryWriter ifTrue, InventoryWriter ifFalse, Predicate<ItemEntry> filter) {
			this.ifTrue = ifTrue;
			this.ifFalse = ifFalse;
			this.filter = filter;
		}
		@Override
		public int insert(ItemEntry ent, int amount) {
			if(filter.test(ent)) 
				return ifTrue.insert(ent, amount);
			return ifFalse.insert(ent, amount);
		}
		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			return 0;
		}
		@Override
		public int toInsertBulk(RecipeOutput outItems, int amount) {
			return 0;
		}
		@Override
		public int toInsert(ItemEntry item, int amount) {
			return 0;
		}
		
	}
	/**
	 * Writes items to inventory, only if items match the filter.
	 * Otherwise it rejects them.
	 * @author oskar
	 */
	public static class Filtering implements InventoryWriter{
		private final InventoryWriter writer;
		private final Predicate<ItemEntry> filter;
		/**
		 * Creates a filtering writer
		 * @param writer backing inventory writer
		 * @param filter item filter
		 */
		public Filtering(InventoryWriter writer, Predicate<ItemEntry> filter) {
			this.writer = writer;
			this.filter = filter;
		}
		@Override
		public int insert(ItemEntry ent, int amount) {
			if(filter.test(ent)) 
				return writer.insert(ent, amount);
			return 0;
		}
		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			for(ItemEntry item: block.items()) 
				if(!filter.test(item)) return 0;
			return writer.bulkInsert(block, amount);
		}
		@Override
		public int toInsertBulk(RecipeOutput block, int amount) {
			for(ItemEntry item: block.items()) 
				if(!filter.test(item)) return 0;
			return writer.toInsertBulk(block, amount);
		}
		@Override
		public int toInsert(ItemEntry item, int amount) {
			if(!filter.test(item)) return 0;
			return writer.toInsert(item, amount);
		}
	}
	/**
	 * @author oskar
	 * Writes items to inventory,
	 * preferring the first inventory.
	 * If it fails, tries writing second inventory.
	 * If both fail, rejects some or all items
	 */
	public static class Priority implements InventoryWriter{
		private final InventoryWriter first;
		private final InventoryWriter other;
		/**
		 * Creates a priority writer
		 * @param here preferred inventory
		 * @param other secondary inventory
		 */
		public Priority(InventoryWriter here, InventoryWriter other) {
			this.first = here;
			this.other = other;
		}

		@Override
		public int insert(ItemEntry ent, int amount) {
			int writeFirst = first.insert(ent, amount);
			if(writeFirst == amount) {
				return amount; //all accepted
			}
			int next = amount - writeFirst;
			int writeSecond = other.insert(ent, next);
			return writeFirst+writeSecond;
		}

		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			int writeFirst = first.bulkInsert(block, amount);
			if(writeFirst == amount) {
				return amount; //all accepted
			}
			int next = amount - writeFirst;
			int writeSecond = other.bulkInsert(block, next);
			return writeFirst+writeSecond;
		}

		@Override
		public int toInsertBulk(RecipeOutput block, int amount) {
			int writeFirst = first.toInsertBulk(block, amount);
			if(writeFirst == amount) {
				return amount; //all accepted
			}
			int next = amount - writeFirst;
			int writeSecond = other.toInsertBulk(block, next);
			return writeFirst+writeSecond;
		}

		@Override
		public int toInsert(ItemEntry item, int amount) {
			int writeFirst = first.toInsert(item, amount);
			if(writeFirst == amount) {
				return amount; //all accepted
			}
			int next = amount - writeFirst;
			int writeSecond = other.toInsert(item, next);
			return writeFirst+writeSecond;
		}		
	}
	/**
	 * @author oskar
	 * Writes items to inventories
	 */
	public static class Splitter implements InventoryWriter{
		private final InventoryWriter[] writers;
		/**
		 * Creates a splitter with given writers;
		 * @param writers list of targeted writers
		 */
		public Splitter(InventoryWriter... writers) {
			this.writers = writers;
		}
		private int pos = 0;
		@Override
		public int insert(ItemEntry ent, int amount) {
			next();
			int remaining = amount;
			int transferred = 0;
			for(int i = 0; i < writers.length; i++) {
				InventoryWriter writer = writers[pos];
				int now = writer.insert(ent, remaining);
				remaining -= now;
				transferred += now;
				if(remaining == 0) return transferred;
				next();
			}
			return transferred;
		}
		
		private int next() {
			pos++;
			if(pos >= writers.length) pos = 0;
			return pos;
		}

		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			next();
			int remaining = amount;
			int transferred = 0;
			for(int i = 0; i < writers.length; i++) {
				InventoryWriter writer = writers[pos];
				int now = writer.bulkInsert(block, remaining);
				remaining -= now;
				transferred += now;
				if(remaining == 0) return transferred;
				next();
			}
			return transferred;
		}

		@Override
		public int toInsertBulk(RecipeOutput outItems, int amount) {
			return 0;
		}

		@Override
		public int toInsert(ItemEntry item, int amount) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
