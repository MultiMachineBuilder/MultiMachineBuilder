/**
 * 
 */
package mmb.WORLD.items;

import mmb.WORLD.item.Item;
import mmb.WORLD.tool.DumpItems;
import mmb.WORLD.tool.WindowTool;

/**
 * @author oskar
 *
 */
public class Bucket extends Item {
	private static final DumpItems tool = new DumpItems();

	@Override
	public WindowTool getTool() {
		return tool;
	}
}
