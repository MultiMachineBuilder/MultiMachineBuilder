/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 * A dataless skeletal block entity.
 */
public abstract class SkeletalBlockEntityDataless extends SkeletalBlockEntity{
	@Override
	public void load(JsonNode data) {
		//unused
	}
	protected SkeletalBlockEntityDataless(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	@Override
	public final JsonNode save() {
		return new TextNode(type().id());
	}
}
