/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class PickaxeWorkbench extends Block implements BlockActivateListener {

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		PickaxeGUI gui = new PickaxeGUI(window);
		window.openAndShowWindow(gui, "Pickaxe workbench");
	}
	
	public static Map<ItemEntry, ItemType> recipes = new HashMap<>();
	static {
		recipes.put(ContentsItems.pickHeadWood, ContentsItems.pickWood);
		recipes.put(ContentsItems.pickHeadRudimentary, ContentsItems.pickRudimentary);
	}
}
