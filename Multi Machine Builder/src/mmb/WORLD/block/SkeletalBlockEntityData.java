/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public abstract class SkeletalBlockEntityData extends SkeletalBlockEntity{
	protected SkeletalBlockEntityData(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	private static final Debugger debug = new Debugger("BLOCKS");
	@Override
	public final JsonNode save() {
		ObjectNode data = JsonTool.newObjectNode();
		data.put("blocktype", type().id());
		try {
			save0(data);
		} catch (Exception e) {
			debug.pstm(e, "Failed to write JSON data");
		}
		return data;
	}
	/**
	 * Save additional supported data
	 * @param node ObjectNode, to which data will be saved
	 */
	protected abstract void save0(ObjectNode node);
}
