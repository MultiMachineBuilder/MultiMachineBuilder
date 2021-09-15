/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * @author oskar
 * A dataless skeletal block entity.
 */
public abstract class BlockEntityDataless extends BlockEntity{
	@Override
	public void load(JsonNode data) {
		//unused
	}
	@Override
	public final JsonNode save() {
		return new TextNode(type().id());
	}
}
