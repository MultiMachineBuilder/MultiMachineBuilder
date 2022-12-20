/**
 * 
 */
package mmb.engine.json;

import java.awt.Color;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.NN;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class Save {
	private Save() {}
	@NN public static <T> FixedGrid<T> loadGrid(Function<JsonNode, @NN T> itemLoader, ArrayNode array){
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
	@NN public static <T> ArrayNode saveGrid(Function<T, JsonNode> itemSaver, Grid<T> grid) {
		ArrayNode array = JsonTool.newArrayNode();
		array.add(grid.width());
		array.add(grid.height());
		for(T item: grid) {
			JsonNode save = itemSaver.apply(item);
			array.add(save);
		}
		return array;
	}
	
	@NN public static ArrayNode saveColor(Color c) {
		ArrayNode result = JsonTool.newArrayNode();
		result.add(c.getRed());
		result.add(c.getGreen());
		result.add(c.getBlue());
		result.add(c.getAlpha());
		return result;
	}
	@NN public static Color loadColor(JsonNode node) {
		int r = node.get(0).asInt(0);
		int g = node.get(1).asInt(0);
		int b = node.get(2).asInt(0);
		int a = node.get(3).asInt(255);
		return new Color(r, g, b, a);
	}
}
