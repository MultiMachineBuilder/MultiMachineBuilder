/**
 * 
 */
package mmb.content.craft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntList;
import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.engine.block.Block;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A crafting table
 * @author oskar
 */
public class ManCrafter extends Block implements BlockActivateListener {
	/** The size of this crafting table */
	public final int size;
	/** The tier of the crafting table */
	public final int tier;
	private ManCrafter(int tier) {
		size = sizes.getInt(tier);
		this.tier = tier;
	}
	
	//Tiers
	/** List of crafting table sizes*/
	public static final IntList sizes = IntList.of(3, 4, 5, 7, 9, 10);
	/** List of all crafting tables */
	public static final List<@NN Block> types;
	static {
		int n = sizes.size();
		Block[] ttypes = new Block[n];
		
		for(int i = 0; i < n; i++) {
			ttypes[i] = initCraftingType(i);
		}
		types = Collections.unmodifiableList(Arrays.asList(ttypes));
	}
	/** Initializes crafting tables */
	public static void init(){
		//just for init
	}

	static Block initCraftingType(int tier) {
		int increased = tier + 1;
		String textureName = "machine/assembly "+increased+".png";
		String idName = "crafting."+increased;
		String title = GlobalSettings.$res("machine-crafter")+" "+increased;
		
		Block type = new ManCrafter(tier);
		type.title(title);
		type.texture(textureName);
		type.register(idName);
		return type;
	}
	
	@Nil private CraftGUI component;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(component != null) return;
		component = new CraftGUI(size, window.getPlayer().inv, this, window);
		window.openAndShowWindow(component, "Tier "+tier+" crafting");
	}
	public void closeWindow(WorldWindow window) {
		CraftGUI component0 = component;
		if(component0 == null) return;
		window.closeWindow(component0);
		component = null;
	}
}
