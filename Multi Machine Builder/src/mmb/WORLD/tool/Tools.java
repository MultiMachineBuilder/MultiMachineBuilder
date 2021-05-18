/**
 * 
 */
package mmb.WORLD.tool;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Nonnull;

import mmb.WORLD.gui.window.WorldWindow;
import mmb.debug.Debugger;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Tools {
	private static final Debugger debug = new Debugger("TOOLS");
	/**
	 * A list of all registered tools
	 */
	public static final SelfSet<String, WindowToolModel> toollist = new HashSelfSet<>();
	/**
	 * A standard tool
	 */
	public static final WindowToolModel TOOL_STANDARD =
			new WindowToolModel(ToolStandard.ICON_NORMAL, () -> new ToolStandard(), "standard");
	public static final WindowToolModel TOOL_PAINT =
			new WindowToolModel(ToolPaint.icon, () -> new ToolPaint(), "paint");

	private static boolean initialized = false;
	/**
	 * Initializes tool list
	 */
	public static void initialize() {
		if(initialized) return;
		toollist.add(TOOL_STANDARD);
		toollist.add(TOOL_PAINT);
		debug.printl("Tools initialized");
		initialized = true;
		
	}
	/**
	 * Creates a new array of window tools
	 * @return a new array of window tools
	 */
	@Nonnull public static WindowTool[] createWindowTools() {
		WindowTool[] result = new WindowTool[toollist.size()];
		int i = 0;
		for(WindowToolModel wtm: toollist) {
			result[i] = wtm.create();
			i++;
		}
		return result;
	}
	/**
	 * Creates a list of window tools, writing it to given generic collection
	 * @param c input collection
	 * @throws NullPointerException if {@code c} is null
	 */
	public static void createWindowTools(@Nonnull Collection<? super WindowTool> c, WorldWindow window) {
		Objects.requireNonNull(c, "c is null");
		for(WindowToolModel wtm: toollist) {
			WindowTool tool = wtm.create();
			tool.setWindow(window);
			c.add(tool);
		}
	}
}
