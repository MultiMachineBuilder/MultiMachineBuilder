/**
 * 
 */
package mmb.data.json;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author oskar
 *
 */
public class JsonTool {
	@SuppressWarnings("null")
	@Nonnull public static ObjectNode newObjectNode() {
		return JsonNodeFactory.instance.objectNode();
	}
	@SuppressWarnings("null")
	@Nonnull public static ArrayNode newArrayNode() {
		return JsonNodeFactory.instance.arrayNode();
	}
	@Nonnull public static ObjectNode requestObject(String name, ObjectNode node) {
		JsonNode result = node.get(name);
		if(result instanceof ObjectNode) {
			return (ObjectNode) result;
		}
		return newObjectNode();
	}
	@Nonnull public static ArrayNode requestArray(String name, ObjectNode node) {
		JsonNode result = node.get(name);
		if(result instanceof ArrayNode) {
			return (ArrayNode) result;
		}
		return newArrayNode();
	}
	public static final ObjectMapper mapper;
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
	public static JsonNode saveNode(@Nullable Object node) {
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

	public static <T> T[] loadArray(Class<T> itemType, Function<JsonNode, T> converter, ArrayNode data) {
		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(itemType, data.size());
		return loadToArray(converter, data, result);
	}
	public static <T> T[] loadToArray(Function<JsonNode, T> converter, ArrayNode data, T[] tgt) {
		for(int i = 0; i < tgt.length; i++) {
			tgt[i] = converter.apply(data.get(i));
		}
		return tgt;
	}
	@SuppressWarnings("null")
	public static <T> ArrayNode saveArray(Function<T, JsonNode> converter, T[] data) {
		return saveArray(converter, Arrays.asList(data));
	}
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
}
