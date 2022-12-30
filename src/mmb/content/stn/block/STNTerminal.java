/**
 * 
 */
package mmb.content.stn.block;

import mmb.NN;
import mmb.Nil;
import mmb.content.stn.STN;
import mmb.content.stn.network.STNNetworkProcessing.STNRGroupTag;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.worlds.world.World;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class STNTerminal extends STNBaseMachine implements BlockActivateListener {

	@NN private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/terminal.png");
	@Override
	public BlockType type() {
		return STN.STN_terminal;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	protected STNBaseMachine blockCopy0() {
		return new STNTerminal();
	}

	//Storage and recipes not supported
	@Override
	public Inventory storage() {
		return null; //NOSONAR the method is declared nullable
	}
	@Override
	public STNRGroupTag oldrecipes() {
		return null;
	}
	@Override
	public Inventory oldstorage() {
		return null; //NOSONAR the method is declared nullable
	}
	@Override
	public STNRGroupTag recipes() {
		return null;
	}

	STNTerminalGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(gui != null) return;
		if(window == null) return;
		gui = new STNTerminalGUI(this, window);
		window.openAndShowWindow(gui, "STN term");
	}

}
