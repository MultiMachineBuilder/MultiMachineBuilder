/**
 * 
 */
package mmb.WORLD.crafting;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * A class which allows to make recipes using strings
 */
public class RecipeFormatter {
	private final Char2ObjectMap<@Nonnull ItemEntry> map = new Char2ObjectOpenHashMap<>();
	public RecipeFormatter(String src, ItemEntry... dest) {
		int length = Math.min(src.length(), dest.length);
		for(int i = 0; i < length; i++) {
			ItemEntry ent = dest[i];
			if(ent == null) throw new NullPointerException("Item "+i+" is null");
			map.put(src.charAt(i), ent);
		}
	}
	public Grid<ItemEntry> format(String string, int size) {
		int sqr = size * size;
		int length = string.length();
		if(length < sqr)
			throw new IllegalArgumentException("Expected at least "+size+" * "+size+" = "+sqr+" characters, got "+length);
		Grid<ItemEntry> result = new FixedGrid<>(size, size);
		int i = 0;
		for(int y = 0; y < size; y++) {
			for(int x = 0; x < size; x++) {
				ItemEntry test = map.get(string.charAt(i));
				result.set(x, y, test);
				++i;
			}
		}
		return result;
	}
}
