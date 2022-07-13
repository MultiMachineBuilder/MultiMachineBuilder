/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.gui.window.WorldWindow;
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
}
