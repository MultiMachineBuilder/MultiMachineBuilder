/**
 * 
 */
package mmb.WORLD.items;

import mmb.WORLD.item.Item;
import mmb.WORLD.tool.ConfigureDroppedItemExtractors;
import mmb.WORLD.tool.WindowTool;

/**
 * @author oskar
 *
 */
public class ConfigExtractors extends Item {
	private static final ConfigureDroppedItemExtractors tool = new ConfigureDroppedItemExtractors();

	@Override
	public WindowTool getTool() {
		return tool;
	}
	
}
