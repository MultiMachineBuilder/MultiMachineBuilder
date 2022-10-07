/**
 * 
 */
package mmb.world.block;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * @author oskar
 * A definition for a block that does not hold any data.
 */
public abstract class BlockEntityDataless extends BlockEntity implements Cloneable{
	@Override
	public void load(@Nullable JsonNode data) {
		//unused
	}
	@Override
	public final JsonNode save() {
		return new TextNode(type().id());
	}
	@Override
	public final BlockEntity blockCopy() {
		return (BlockEntity) type().createBlock();
	}
}
