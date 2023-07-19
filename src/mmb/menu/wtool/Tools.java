/**
 * 
 */
package mmb.menu.wtool;

import java.util.Collection;
import java.util.Objects;

import mmb.NN;
import mmb.content.aim.ToolAim;
import mmb.content.drugs.Alcohol;
import mmb.content.drugs.ToolAlcohol;
import mmb.content.imachine.extractor.ConfigureDroppedItemExtractors;
import mmb.content.pickaxe.ToolPickaxe;
import mmb.engine.debug.Debugger;
import mmb.engine.visuals.ToolVisuals;
import mmb.menu.world.window.WorldWindow;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * A set of tool utilities
 * @author oskar
 *
 */
public class Tools {
	private Tools() {}
	private static final Debugger debug = new Debugger("TOOLS");
	
	/**
	 * A list of all registered tools
	 */
	public static final SelfSet<@NN String, @NN WindowToolModel> toollist = HashSelfSet.createNonnull(WindowToolModel.class);
	/**
	 * A standard tool
	 */
	public static final WindowToolModel TOOL_STANDARD =
			new WindowToolModel(ToolStandard.ICON_NORMAL, ToolStandard::new, "standard");
	public static final WindowToolModel TOOL_PAINT =
			new WindowToolModel(ToolPaint.icon, ToolPaint::new, "paint");
	public static final WindowToolModel TOOL_COPY =
			new WindowToolModel(Copy.icon, Copy::new, "copy");
	public static final WindowToolModel TOOL_PICKERS =
			new WindowToolModel(ConfigureDroppedItemExtractors.icon, ConfigureDroppedItemExtractors::new, "droppedItems");
	public static final WindowToolModel TOOL_PICKAXE =
			new WindowToolModel(ToolPickaxe.icon, ToolPickaxe::new, "pickaxe");
	public static final WindowToolModel TOOL_VISUALS =
			new WindowToolModel(ToolVisuals.ICON, ToolVisuals::new, "visuals");
	public static final WindowToolModel TOOL_AIM =
			new WindowToolModel(ToolAim.icon, ToolAim::new, "aim");

	private static boolean initialized = false;
	/** Initializes tool list */
	public static void init() {
		if(initialized) return;
		toollist.add(TOOL_STANDARD);
		toollist.add(TOOL_PAINT);
		toollist.add(TOOL_COPY);
		toollist.add(TOOL_PICKERS);
		toollist.add(TOOL_PICKAXE);
		toollist.add(TOOL_VISUALS);
		
		toollist.add(TOOL_AIM);
		debug.printl("Tools initialized");
		initialized = true;
		
	}
	/**
	 * Creates a new array of window tools
	 * @return a new array of window tools
	 */
	@NN public static WindowTool[] createWindowTools() {
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
	 * @param window world window to use
	 * @throws NullPointerException if {@code c} is null
	 */
	public static void createWindowTools(Collection<? super WindowTool> c, WorldWindow window) {
		Objects.requireNonNull(c, "c is null");
		for(WindowToolModel wtm: toollist) {
			WindowTool tool = wtm.create();
			tool.setWindow(window);
			c.add(tool);
		}
	}
}
