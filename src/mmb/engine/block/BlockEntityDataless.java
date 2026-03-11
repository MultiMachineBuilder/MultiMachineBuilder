/**
 * 
 */
package mmb.engine.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.annotations.Nil;

/**
 * A block entity that does not hold any data.
 * @author oskar
 */
public abstract class BlockEntityDataless extends BlockEntity implements Cloneable{
	@Override
	public final JsonNode save() {
		return new TextNode(itemType().id());
	}
	@Override
	public final BlockEntity blockCopy() {
		return (BlockEntity) itemType().createBlock(null);
	}
}
