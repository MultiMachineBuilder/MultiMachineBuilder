/**
 * 
 */
package mmb.menu.world.window;

import mmb.NN;
import mmb.Nil;
import mmb.data.variables.ListenableValue;
import mmb.engine.debug.Debugger;
import mmb.engine.worlds.world.Player;
import mmb.menu.wtool.WindowTool;

/**
 * Handles tool selection in the world window
 * @author oskar
 */
public class ToolSelectionModel {
	private WindowTool toolIL;
	private WindowTool toolTL;
	/** This tool selection model's world window */
	@NN public final WorldWindow window;
	private static final Debugger debug = new Debugger("TOOL SELECTOR");
	/**
	 * Creates a tool selection model
	 * @param window world window
	 */
	public ToolSelectionModel(WorldWindow window) {
		this.window = window;
		tool.listenrem(this::toolRemoved);
		tool.listenadd(this::toolAdded);
	}

	/**
	 * Invoked when an item is selected from an item list 
	 * @param tool1 newly selected tool
	 */
	public void toolSelectedItemList(@Nil WindowTool tool1) {
		debug.printl("IL tool: "+tool1);
		toolIL = tool1;
		resetTools();
	}
	/** Invoked when an item is selected from a tool list
	 * @param tool1 newly selected tool
	 */
	public void toolSelectedToolList(@Nil WindowTool tool1) {
		debug.printl("TL tool: "+tool1);
		toolTL = tool1;
		resetTools();
	}
	
	private void resetTools() {
		debug.printl("Reset tools");
		WorldWindow window0 = window;
		if(toolTL == null) {
			setTool(toolIL);
		}else{
			Player p = window0.getPlayer();
			if(!(p == null || p.isCreative())) {
				if(toolIL == null) {
					setTool(window0.std);
				}else{
					setTool(toolIL);
				}
			}else{
				setTool(toolTL);
			}
		}
	}
	private void toolRemoved(@Nil WindowTool t) {
		if(t != null) {
			t.deselected();
			debug.printl("Old tool: " + t.id());
		}else {
			debug.printl("Old tool: null");
		}
	}
	private void toolAdded(@Nil WindowTool t) { //sets up the new tool
		if(t != null) {
			t.selected();
			t.setWindow(window);
			debug.printl("New tool: " + t.id());
		}else {
			debug.printl("New tool: null");
		}
	}

	/** The window tool currently used. Sets the tool directly */
	@NN public final ListenableValue<@Nil WindowTool> tool = new ListenableValue<>(null);
	/**
	 * Directly sets the current tool
	 * @param newTool tool to use
	 */
	public void setTool(@Nil WindowTool newTool) {
		tool.set(newTool);
	}
	/** @return current window tool */
	public WindowTool getTool() {
		return tool.get();
	}
}
