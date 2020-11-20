/**
 * 
 */
package mmb.WORLD.tileworld.tool;

import java.util.ArrayList;
import java.util.List;

import mmb.WORLD.tileworld.tools.ToolMine;
import mmb.WORLD.tileworld.tools.ToolPlace;
import mmb.WORLD.tileworld.tools.ToolTextEditor;
import mmb.WORLD.tileworld.tools.ToolVoid;

/**
 * @author oskar
 *
 */
public class Tools {
	private static final List<BlockTool> tools = new ArrayList<BlockTool>();
	public static BlockTool[] getTools() {
		return tools.toArray(new BlockTool[tools.size()]);
	}
	public static void addTool(BlockTool t) {
		tools.add(t);
	}
	public static void create() {
		//ToolMine.create();
		addTool(new ToolMine());
		addTool(new ToolPlace(true));
		addTool(new ToolPlace(false));
		addTool(new ToolCopyPaste());
		addTool(new ToolTextEditor());
		addTool(new ToolVoid());
	}
}
