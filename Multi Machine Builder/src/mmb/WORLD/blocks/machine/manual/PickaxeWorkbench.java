/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import java.util.HashMap;
import java.util.Map;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class PickaxeWorkbench extends Block implements BlockActivateListener {

	@Override
	public void click(int blockX, int blockY, World map, WorldWindow window, double partX, double partY) {
		if(window == null) return;
		PickaxeGUI gui = new PickaxeGUI(window);
		window.openAndShowWindow(gui, "Pickaxe workbench");
	}
	
	public static Map<ItemEntry, ItemType> recipes = new HashMap<>();
	{
		
	}
}
