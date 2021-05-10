/**
 * 
 */
package mmb.WORLD.tool;

import javax.swing.Icon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mmb.WORLD.gui.window.WorldWindow;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ToolSelectionModel {
	private WindowTool toolIL;
	private WindowTool toolTL;
	public final WorldWindow window;
	private static final Debugger debug = new Debugger("TOOL SELECTOR");
	public ToolSelectionModel(WorldWindow window) {
		this.window = window;
	}

	public void toolSelectedItemList(WindowTool tool) {
		toolIL = tool;
		resetTools();
	}
	public void toolSelectedToolList(WindowTool tool) {
		toolTL = tool;
		resetTools();
	}
	
	private void resetTools() {
		if(toolTL == null) setTool(toolIL);
		else {
			if(window != null && !window.getPlayer().creative.getValue()) setTool(toolIL);
			else setTool(toolTL);
		}
	}

	private WindowTool tool;
	public void setTool(WindowTool newTool) {
		
		if(tool == newTool) return;
		if(tool != null) {
			tool.deselected();
			debug.printl("Old tool: " + tool.id());
		}else {
			debug.printl("Old tool: null");
		}
		if(newTool != null) {
			newTool.selected();
			newTool.setWindow(window);
			debug.printl("New tool: " + newTool.id());
		}else {
			debug.printl("New tool: null");
		}
		tool = newTool;
	}
	public WindowTool getTool() {
		return tool;
	}

	//TEST
	private static class DummyTool extends WindowTool{
		protected DummyTool(String s) {
			super(s);
		}

		@Override
		public String title() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Icon getIcon() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public static void main(String[] args) {
		test();
	}
	@Test static void test() {
		//WorldWindow dummyWindow = new WorldWindow();
		ToolSelectionModel test = new ToolSelectionModel(null);
		WindowTool tool1 = new DummyTool("Tool1");
		WindowTool tool2 = new DummyTool("Tool2");
		Assertions.assertNull(test.getTool(), "Tool was not yet selected");
		test.toolSelectedToolList(tool1);
		Assertions.assertSame(tool1, test.getTool(), "Tool1 not properly selected");
		test.toolSelectedToolList(tool2);
		Assertions.assertSame(tool2, test.getTool(), "Tool2 not properly selected");
		test.toolSelectedToolList(tool1);
		Assertions.assertSame(tool1, test.getTool(), "Tool1 not properly selected");
	}
}
