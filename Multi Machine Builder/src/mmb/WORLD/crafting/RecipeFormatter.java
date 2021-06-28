/**
 * 
 */
package mmb.WORLD.crafting;

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
	private final Char2ObjectMap<ItemEntry> map = new Char2ObjectOpenHashMap<>();
	public RecipeFormatter(String src, ItemEntry... dest) {
		int length = Math.min(src.length(), dest.length);
		for(int i = 0; i < length; i++) {
			map.put(src.charAt(i), dest[i]);
		}
	}
	public Grid<ItemEntry> format(String string, int size) {
		int sqr = size * size;
		if(string.length() < sqr)
			throw new IllegalArgumentException(
					String.format("Expected at least {1} * {1} = {2} characters, got {0}", string.length(), size, sqr
				)
			);
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
