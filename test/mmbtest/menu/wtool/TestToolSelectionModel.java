/**
 * 
 */
package mmbtest.menu.wtool;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;

import javax.swing.Icon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mmb.menu.world.window.ToolSelectionModel;
import mmb.menu.wtool.WindowTool;

/**
 * @author oskar
 *
 */
class TestToolSelectionModel {

	//TEST
	public static class DummyTool extends WindowTool{
		public DummyTool(String s) {
			super(s);
		}
	
		@Override
		public String title() {
			return "Dummy tool";
		}
	
		@Override
		public Icon getIcon() {
			return null;
		}
	
		@Override
		public void preview(int startX, int startY, double scale, Graphics g) {
			// unused
		}
		
	}



	@Test void test() {
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
