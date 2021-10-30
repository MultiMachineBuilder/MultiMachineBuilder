/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * @author oskar
 * A dataless skeletal block entity.
 */
public abstract class BlockEntityDataless extends BlockEntity{
	@Override
	public void load(@Nullable JsonNode data) {
		//unused
	}
	@Override
	public final JsonNode save() {
		return new TextNode(type().id());
	}
}
