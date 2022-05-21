/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nonnull;
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
		return clone();
	}
	
	/**
	 * Creates a copy of this block entity.
	 * The returned object has no owner and position, so it can be immediately pasted.
	 */
	@Override
	@Nonnull public BlockEntity clone(){
		try {
			BlockEntity copy = (BlockEntity)super.clone();
			copy.resetMap(null, 0, 0);
			copy.clearListeners();
			return copy;
		}catch(CloneNotSupportedException e) {
			throw new InternalError(e); //last safeguard, if Cloneable fails
		}
	}
}
