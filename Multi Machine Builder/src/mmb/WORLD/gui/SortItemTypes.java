/**
 * 
 */
package mmb.WORLD.gui;

import java.util.Comparator;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public enum SortItemTypes implements Comparator<ItemType> {
	DESC_TITLE("Titles descending") {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o2.title().compareTo(o1.title());
		}
	}, ASC_TITLE("Titles ascending") {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o1.title().compareTo(o2.title());
		}
	}, DESC_ID("IDs descending") {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o2.id().compareTo(o1.id());
		}
	}, ASC_ID("IDs ascending") {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o1.id().compareTo(o2.id());
		}
	};

	@Override
	public abstract int compare(ItemType o1, ItemType o2);
	public final String title;
	SortItemTypes(String title) {
		this.title = title;
	}

}
