/**
 * 
 */
package mmb.DATA.json;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	public static ObjectNode newObjectNode() {
		return JsonNodeFactory.instance.objectNode();
	}
	public static ArrayNode newArrayNode() {
		return JsonNodeFactory.instance.arrayNode();
	}
	public static ObjectNode requestObject(String name, ObjectNode node) {
		JsonNode result = node.get(name);
		if(result instanceof ObjectNode) {
			return (ObjectNode) result;
		}
		return newObjectNode();
	}
	public static ArrayNode requestArray(String name, ObjectNode node) {
		JsonNode result = node.get(name);
		if(result instanceof ObjectNode) {
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
	public static String save(JsonNode node) throws JsonProcessingException {
		return writer.writeValueAsString(node);
	}
}
