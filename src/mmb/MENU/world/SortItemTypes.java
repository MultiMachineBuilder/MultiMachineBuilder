/**
 * 
 */
package mmb.MENU.world;

import static mmb.GlobalSettings.$res;

import java.util.Comparator;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public enum SortItemTypes implements Comparator<ItemType> {
	DESC_TITLE($res("wgui-titledesc")) {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o2.title().compareTo(o1.title());
		}
	},
	ASC_TITLE($res("wgui-titleasc")) {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o1.title().compareTo(o2.title());
		}
	},
	DESC_ID($res("wgui-iddesc")) {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o2.id().compareTo(o1.id());
		}
	},
	ASC_ID($res("wgui-idasc")) {
		@Override
		public int compare(ItemType o1, ItemType o2) {
			return o1.id().compareTo(o2.id());
		}
	};

	public final String title;
	SortItemTypes(String title) {
		this.title = title;
	}

}
