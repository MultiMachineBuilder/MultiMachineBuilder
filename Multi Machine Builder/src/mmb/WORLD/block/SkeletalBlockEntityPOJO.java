/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 * A block which stores data as Plain Old Java Object.
 * It is very easy to use - just specify expected type and behavior via optional overrides.
 * <br>Note: The type should be {@link JsonSerializable}
 * @param <T> expected type
 */
public abstract class SkeletalBlockEntityPOJO<T> extends SkeletalBlockEntityData {
	@Nonnull protected final Class<T> expType;
	private static final Debugger debug = new Debugger("BLOCK LOADER");
	@Nonnull protected T data = defaultValue();
	protected SkeletalBlockEntityPOJO(int x, int y, BlockMap owner, Class<T> t) {
		super(x, y, owner);
		expType = t;
	}

	@Override
	public BlockType type() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("null")
	@Override
	public void load(@Nonnull JsonNode data) {
		JsonNode subnode = data.get("data");
		try {
			T newData = JsonTool.loadPOJO((TreeNode)subnode, expType);
			if(newData == null) {
				debug.printl("Got null value");
			}else {
				this.data = newData;
			}
		} catch (JsonProcessingException | IllegalArgumentException e) {
			debug.pstm(e, "Failed to load the block");
		}
	}

	@Override
	protected void save0(ObjectNode node) {
		JsonNode result;
		result = JsonTool.saveNode(data);
		node.set("data", result);
	}
	@Nonnull abstract protected T defaultValue();

}
