/**
 * 
 */
package mmb.DATA;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.DATA.json.JsonTool;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class SaveGrids {
	@Nonnull public static <T> FixedGrid<T> loadGrid(Function<JsonNode, T> itemLoader, ArrayNode array){
		int width = array.get(0).asInt();
		int height = array.get(1).asInt();
		FixedGrid<T> grid = new FixedGrid<>(width, height);
		int i = 2;
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				JsonNode idata = array.get(i);
				grid.set(x, y, itemLoader.apply(idata));
				i++;
			}
		}
		return grid;
	}
	@Nonnull public static <T> ArrayNode saveGrid(Function<T, JsonNode> itemSaver, Grid<T> grid) {
		ArrayNode array = JsonTool.newArrayNode();
		array.add(grid.width());
		array.add(grid.height());
		for(T item: grid) {
			JsonNode save = itemSaver.apply(item);
			array.add(save);
		}
		return array;
	}
}
