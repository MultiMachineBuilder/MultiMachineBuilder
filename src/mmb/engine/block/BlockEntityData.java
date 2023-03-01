/**
 * 
 */
package mmb.engine.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.engine.debug.Debugger;
import mmb.engine.json.JsonTool;

/**
 * A block entity with data support
 * @author oskar
 */
public abstract class BlockEntityData extends BlockEntity{
	private static final Debugger debug = new Debugger("BLOCKS");
	@Override
	public final JsonNode save() {
		ObjectNode data = JsonTool.newObjectNode();
		data.put("blocktype", type().id());
		try {
			save0(data);
		} catch (Exception e) {
			debug.stacktraceError(e, "Failed to write JSON data");
		}
		return data;
	}
	/**
	 * Save additional supported data
	 * @param node ObjectNode, to which data will be saved
	 */
	protected abstract void save0(ObjectNode node);
}
