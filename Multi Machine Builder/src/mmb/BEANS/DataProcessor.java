/**
 * 
 */
package mmb.BEANS;

import java.awt.Color;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.DATA.json.JsonTool;

/**
 * @author oskar
 *
 */
public class DataProcessor {
	public static ArrayNode saveColor(Color c) {
		ArrayNode result = JsonTool.newArrayNode();
		result.add(c.getRed());
		result.add(c.getGreen());
		result.add(c.getBlue());
		result.add(c.getAlpha());
		return result;
	}
	
	public static Color loadColor(JsonNode node) {
		int r = node.get(0).asInt(0);
		int g = node.get(1).asInt(0);
		int b = node.get(2).asInt(0);
		int a = node.get(3).asInt(255);
		return new Color(r, g, b, a);
	}
}
