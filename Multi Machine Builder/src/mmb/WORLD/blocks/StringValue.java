/**
 * 
 */
package mmb.WORLD.blocks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import mmb.WORLD.block.properties.BlockProperty;

/**
 * @author oskar
 *
 */
public class StringValue implements BlockProperty {
	public String value;
	@Override
	public void load(JsonNode e) {
		value = e.asText("");
	}

	@Override
	public JsonNode save() {
		return new TextNode(value);
	}

	@Override
	public String name() {
		return "value";
	}

	public StringValue(String value) {
		this.value = value;
	}
	public StringValue() {
		value = "";
	}
}
