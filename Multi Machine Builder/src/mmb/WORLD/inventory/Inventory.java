/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;
import org.joml.Vector3d;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public interface Inventory extends Iterable<ItemRecord>, RecipeOutput {
	
	@Override
	default void produceResults(InventoryWriter tgt, int amount) {
		for(ItemRecord record: this) {
			tgt.write(record.item(), record.amount()*amount);
		}
	}
	@Override
	default void calcVolumes(Vector3d out) {
		out.x += volume();
		out.y += volume();
		out.z += volume();
	}
	@Override
	default void represent(PicoWriter out) {
		for(ItemRecord record: this) {
			out.writeln(record.toRecipeOutputString());
		}
	}
	@Override
	@Nonnull public Iterator<ItemRecord> iterator();
	/**
	 * Get the item record under given item type
	 * @param entry
	 * @return the item record with given type, or throws if not found
	 */
	@Nonnull public ItemRecord get(ItemEntry entry);
	/**
	 * Get the item record under given item type
	 * @param entry
	 * @return the item record with given type, or null if not found
	 */
	@Nullable public ItemRecord nget(ItemEntry entry);
	public int insert(ItemEntry ent, int amount);
	public int extract(ItemEntry ent, int amount);
	/**
	 * @return capacity of given inventory
	 */
	public double capacity();
	/**
	 * @return used volume of given inventory
	 */
	public double volume();
	/**
	 * @return does given inventory exist?
	 */
	default public boolean exists() {
		return true;
	}
	default public boolean canExtract() {
		return true;
	}
	default public boolean canInsert() {
		return true;
	}

	@Nonnull default public Inventory lockInsertions() {
		return ExtractionsOnlyInventory.decorate(this);
	}
	@Nonnull default public Inventory lockExtractions() {
		return InsertionsOnlyInventory.decorate(this);
	}
	@Nonnull default public Inventory readOnly() {
		return ReadOnlyInventory.decorate(this);
	}

	public default InventoryReader createReader() {
		return new InventoryReader() {
			private final Iterator<ItemRecord> records = iterator();
			private ItemRecord current = records.hasNext() ? records.next() : null;
			
			@Override
			public int currentAmount() {
				if(current == null) return 0;
				return current.amount();
			}

			@Override
			public ItemEntry currentItem() {
				if(current == null) return null;
				return current.item();
			}

			@Override
			public int extract(int amount) {
				if(current == null) return 0;
				return current.extract(amount);
			}

			@Override
			public void skip() {
				current = null;
				records.next();
			}

			@Override
			public int extract(@SuppressWarnings("null") ItemEntry entry, int amount) {
				return extract(entry, amount);
			}

			@Override
			public void next() {
				current = records.next();
			}

			@Override
			public boolean hasNext() {
				return records.hasNext();
			}

			@Override
			public boolean hasCurrent() {
				if(current == null) return false;
				return current.amount() > 0;
			}

			@Override
			public ExtractionLevel level() {
				return ExtractionLevel.RANDOM;
			}
		};
	}
	public default InventoryWriter createWriter() {
		return new InventoryWriter() {
			@Override
			public int write(ItemEntry ent, int amount) {
				return insert(ent, amount);
			}

			@Override
			public int toBeWritten(ItemEntry item, int amount) {
				return insertibleRemain(amount, item);
			}
		};
	}
	
	public default double remainVolume() {
		return capacity() - volume();
	}
	public default double iremainVolume() {
		if(!canInsert()) return 0;
		return capacity() - volume();
	}
	public default int insertibleRemain(int amount, double ivolume) {
		double tvolume = ivolume * amount;
		tvolume = Math.min(iremainVolume(), tvolume) / ivolume;
		return (int) Math.floor(tvolume);
	}
	public default int insertibleRemain(int amount, ItemEntry item) {
		return insertibleRemain(amount, item.volume());
	}
}