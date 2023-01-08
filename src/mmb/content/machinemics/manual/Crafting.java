/**
 * 
 */
package mmb.content.machinemics.manual;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntList;
import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsRecipes;
import mmb.content.CraftingGroups;
import mmb.engine.block.Block;
import mmb.engine.item.Item;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.worlds.world.World;
import mmb.menu.world.inv.CraftGUI;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class Crafting extends Block implements BlockActivateListener {
	/** The size of this crafting table */
	public final int size;
	/** The tier of the crafting table */
	public final int tier;
	public Crafting(int tier) {
		size = sizes.getInt(tier);
		this.tier = tier;
	}
	
	//Tiers
	@NN public static final IntList sizes = IntList.of(3, 4, 5, 7, 9, 10);
	@NN public static final List<@NN Block> types;
	static {
		int n = sizes.size();
		Block[] ttypes = new Block[n];
		
		for(int i = 0; i < n; i++) {
			ttypes[i] = initCraftingType(i);
		}
		types = Collections.unmodifiableList(Arrays.asList(ttypes));
	}
	private static boolean inited = false;
	public static void init(){
		if(inited) return;
		inited = true;
		
		//Generate recipes
		ContentsRecipes.createRecipes();
	}

	public static void ingotNugget(Item ingot, Item nugget) {
		CraftingGroups.crafting.addRecipeGrid(nugget, 4, 4, ingot, 1);
		CraftingGroups.crafting.addRecipeGrid(ingot, 1, 1, nugget, 16);
	}
	
	static Block initCraftingType(int tier) {
		int increased = tier + 1;
		String textureName = "machine/assembly "+increased+".png";
		String idName = "crafting."+increased;
		String title = GlobalSettings.$res("machine-crafter")+" "+increased;
		
		Block type = new Crafting(tier);
		type.title(title);
		type.texture(textureName);
		type.register(idName);
		return type;
	}
	
	private CraftGUI component;
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
