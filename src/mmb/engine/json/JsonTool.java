/**
 * 
 */
package mmb.engine.json;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * A set of JSON utilities
 * @author oskar
 */
public class JsonTool {
	private JsonTool() {}
	
	/** @return a new JSON object with default settings */
	@SuppressWarnings("null")
	@NN public static ObjectNode newObjectNode() {
		return JsonNodeFactory.instance.objectNode();
	}
	/** @return a new JSON array with default settings */
	@SuppressWarnings("null")
	@NN public static ArrayNode newArrayNode() {
		return JsonNodeFactory.instance.arrayNode();
	}
	
	/**
	 * Gets or creates an object node from an JSON object node
	 * @param name object node key
	 * @param node object node to get from
	 * @return an existing object node if found, else a new object node
	 */
	@NN public static ObjectNode requestObject(String name, JsonNode node) {
		JsonNode result = node.get(name);
		if(result instanceof ObjectNode) {
			return (ObjectNode) result;
		}
		return newObjectNode();
	}
	/**
	 * Gets or creates an array node from an JSON object node
	 * @param name object node key
	 * @param node object node to get from
	 * @return an existing array node if found, else a new array node
	 */
	@NN public static ArrayNode requestArray(String name, JsonNode node) {
		JsonNode result = node.get(name);
		if(result instanceof ArrayNode) {
			return (ArrayNode) result;
		}
		return newArrayNode();
	}
	
	/**
	 * Gets an integer from a JSON node
	 * @param name object node key
	 * @param node object node to get from
	 * @param i default return value
	 * @return an integer if found, else the default return value
	 */
	public static int requestInt(JsonNode node, String name, int i) {
		JsonNode result = node.get(name);
		if(result == null) return i;
		return result.asInt(i);
	}
	
	/** Default JSON object writer */
	public static final ObjectMapper mapper;
	/** Default JSON object writer */
	public static final ObjectWriter writer;
	static {
		mapper = new ObjectMapper();
		writer = mapper.writerWithDefaultPrettyPrinter();
	}
	
	/**
	 * @param s input string
	 * @return parsed JSON node
	 * @throws JsonProcessingException when parsing fails
	 */
	public static JsonNode parse(String s) throws JsonProcessingException {
		return mapper.readTree(s);
	}
	/**
	 * 
	 * @param node input JSON node
	 * @return serialized string
	 * @throws JsonProcessingException when serialization fails
	 */
	public static String save(Object node) throws JsonProcessingException {
		return writer.writeValueAsString(node);
	}
	/**
	 * @param node source node
	 * @return JSON node representing the object
	 */
	public static JsonNode saveNode(@Nil Object node) {
		return mapper.valueToTree(node);
	}
	/**
	 * @param <T> expected type
	 * @param node source node
	 * @param type expected type
	 * @return parsed value
	 * @throws JsonProcessingException when parsing fails
	 * @throws IllegalArgumentException at mapper's discretion
	 */
	public static <T> T loadPOJO(TreeNode node, Class<T> type) throws JsonProcessingException, IllegalArgumentException {
		return mapper.treeToValue(node, type);
	}

	/**
	 * Loads a new array from JSON
	 * @param <T> type of the array elements
	 * @param itemType type of the array elements
	 * @param converter converts JSON nodes in the array node to the useful obects
	 * @param data source array node
	 * @return a deserialized array
	 */
	public static <T> T[] loadArray(Class<T> itemType, Function<JsonNode, T> converter, ArrayNode data) {
		@SuppressWarnings({ "unchecked", "null" })
		@NN T[] result = (T[]) Array.newInstance(itemType, data.size());
		return loadToArray(converter, data, result);
	}
	/**
	 * Loads to an existing array from JSON
	 * @param <T> type of the array elements
	 * @param converter converts JSON nodes in the array node to the useful obects
	 * @param data source array node
	 * @param tgt destination array
	 * @return tgt
	 */
	public static <T> T[] loadToArray(Function<JsonNode, T> converter, ArrayNode data, T[] tgt) {
		for(int i = 0; i < tgt.length; i++) {
			tgt[i] = converter.apply(data.get(i));
		}
		return tgt;
	}
	/**
	 * Saves an array to a JSON array node
	 * @param <T> source type
	 * @param converter converts data to JSON nodes
	 * @param data array to be saved
	 * @return a JSON array
	 */
	public static <T> ArrayNode saveArray(Function<T, JsonNode> converter, T[] data) {
		return saveArray(converter, Arrays.asList(data));
	}
	/**
	 * Saves a list to a JSON array node
	 * @param <T> source type
	 * @param converter converts data to JSON nodes
	 * @param data array to be saved
	 * @return a JSON array
	 */
	public static <T> ArrayNode saveArray(Function<T, JsonNode> converter, List<? extends T> data) {
		ArrayNode result = newArrayNode();
		for(T item: data) {
			result.add(converter.apply(item));
		}
		return result;
	}
	/**
	 * Retrieves an integer from a JsonNode by name
	 * @param node the JSON node
	 * @param string name of key
	 * @param i default value
	 * @return value located in given JSON key, or default value if absent or invalid
	 */
	public static int getInt(JsonNode node, String string, int i) {
		JsonNode node0 = node.get(string);
		if(node0 == null) return i;
		return node0.asInt(i);
	}

	/** 
	 * Loads a grid from JSON
	 * @param <T> type of the elements
	 * @param itemLoader converts JSON nodes in the array node to the useful obects
	 * @param array saved grid data
	 * @return a deserialized grid
	 */
	@NN public static <T> FixedGrid<T> loadGrid(Function<JsonNode, T> itemLoader, ArrayNode array){
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
	/**
	 * @param <T> type of the elements
	 * @param itemSaver converter converts JSON nodes in the array node to the useful obects
	 * @param grid grid to be saved
	 * @return a serialized array node
	 */
	@NN public static <T> ArrayNode saveGrid(Function<T, JsonNode> itemSaver, Grid<T> grid) {
		ArrayNode array = newArrayNode();
		array.add(grid.width());
		array.add(grid.height());
		for(T item: grid) {
			JsonNode save = itemSaver.apply(item);
			array.add(save);
		}
		return array;
	}

	/**
	 * Saves the color as RGBA JSON array
	 * @param c color to be saved
	 * @return serialized color data
	 */
	@NN public static ArrayNode saveColor(Color c) {
		ArrayNode result = newArrayNode();
		result.add(c.getRed());
		result.add(c.getGreen());
		result.add(c.getBlue());
		result.add(c.getAlpha());
		return result;
	}
	/**
	 * Loads a color from JSON
	 * @param node serialized color data
	 * @return RGBA color
	 */
	@NN public static Color loadColor(JsonNode node) {
		int r = node.get(0).asInt(0);
		int g = node.get(1).asInt(0);
		int b = node.get(2).asInt(0);
		int a = node.get(3).asInt(255);
		return new Color(r, g, b, a);
	}

	
}
