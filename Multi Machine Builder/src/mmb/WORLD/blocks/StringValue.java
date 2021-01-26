/**
 * 
 */
package mmb.WORLD.blocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.block.properties.BlockPropertyInfo;

/**
 * @author oskar
 *
 */
public class StringValue implements BlockProperty {
	public String value;
	@Override
	public void load(JsonElement e) {
		value = e.getAsString();
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(value);
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
	
	public static final BlockPropertyInfo bpi_SV = new BlockPropertyInfo() {

		@Override
		public String identifier() {
			return "value";
		}

		@Override
		public BlockProperty createNew() {
			return new StringValue();
		}

		@Override
		public BlockProperty load(JsonElement e) {
			return new StringValue(e.getAsString());
		}

		@Override
		public String title() {
			return "Text";
		}
		
	};

}
