/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntList;
import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;
import monniasza.collects.grid.FixedGrid;
import static mmb.WORLD.blocks.ContentsBlocks.*;
import static mmb.WORLD.blocks.ipipe.Pipes.*;
import static mmb.WORLD.items.ContentsItems.*;
import static mmb.WORLD.contentgen.Materials.*;

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
	public static final IntList sizes = IntList.of(3, 4, 5, 7, 9, 10);
	public static final List<@Nonnull Block> types;
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
		
		//Crafting ingredients
		Craftings.crafting.addRecipe(logs, plank, 16); // wood → planks
		Craftings.crafting.addRecipeGrid(plank, 2, 2, types.get(0), 1); //4*wood plank → crafting table 1
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		plank, plank, plank,
		plank, null,  plank,
		plank, plank, plank
		}, 3, 3, CHEST);
		
		//Pickaxes
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{plank, plank, logs, logs}, 2, 2, PICKBUILDER);
		
		Craftings.crafting.addRecipe(new FixedGrid<>(3,
		plank, logs,  plank,
		null,  plank, null,
		null,  plank, null), pickHeadWood, 1);
		Craftings.crafting.addRecipe(new FixedGrid<>(3,
		Materials.rudimentary.nugget, Materials.rudimentary.base,   Materials.rudimentary.nugget,
		null,                         Materials.rudimentary.nugget, null,
		null,                         Materials.rudimentary.nugget, null), pickHeadRudimentary, 1);
		//Pipes
		Craftings.crafting.addRecipe(new FixedGrid<>(3,
		Materials.iron.base,  null, Materials.iron.base,
		Materials.iron.base,  null, Materials.iron.base,
		Materials.iron.base,  null, Materials.iron.base), STRAIGHT, 24);
		Craftings.crafting.addRecipe(new FixedGrid<>(3,
		Materials.iron.base, Materials.iron.base,  Materials.iron.base,
		Materials.iron.base,  null, null,
		Materials.iron.base,  null, Materials.iron.base), ELBOW, 24);
		Craftings.crafting.addRecipe(new FixedGrid<>(3,
		ELBOW, null,       null,
		null,  Materials.iron.nugget, null,
		null,  null,       ELBOW), DUALTURN, 1);
		Craftings.crafting.addRecipe(new FixedGrid<>(3,
		STRAIGHT, null,       null,
		null,  Materials.iron.nugget, null,
		null,  null,       STRAIGHT), CROSS, 1);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{ELBOW, Materials.iron.nugget, STRAIGHT}, 3, 1, TOLEFT, 1);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{STRAIGHT, Materials.iron.nugget, ELBOW}, 3, 1, TORIGHT, 1);
		
		//Actuator blocks
		//No recipe for Creative Block Placer
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{rod1, bearing1, frame1}, 1, 3, ROTATOR, 1); //Block Rotator
		
		//WireWorld blocks
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{Materials.silicon.base, Materials.copper.base}, 1, 2, ww_wire, 24); //WireWorld cell
		
		//WireWorld gates - 2 inputs
		Craftings.crafting.addRecipe(new FixedGrid<ItemEntry>(3, 2,
		null, YES, null,
		YES, ww_wire, YES), OR, 4); //OR
		Craftings.crafting.addRecipe(new FixedGrid<ItemEntry>(3, 2,
		null, NOT, null,
		YES, ww_wire, YES), XOR, 4); //XOR
		Craftings.crafting.addRecipe(new FixedGrid<ItemEntry>(3, 2,
		null, NOT, null,
		NOT, ww_wire, NOT), AND, 4); //AND
		
		//WireWorld gates - 1 input
		Craftings.crafting.addRecipeGrid(ww_wire, 1, 2, YES); //YES
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{ww_wire, YES}, 1, 2, NOT); //NOT
		
		ItemEntry silicon = Materials.silicon.base;
		ItemEntry iron = Materials.iron.base;
		ItemEntry steel = Materials.steel.base;
		ItemEntry nuggetSteel = Materials.steel.nugget;
		
		//WireWorld signallers
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		silicon , ww_wire, silicon,
		ww_wire, ww_wire, ww_wire,
		silicon, ww_wire, silicon
		}, 3, 3, TRUE, 4); //Always true
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		null,  null,  steel,
		null,  steel, null,
		steel, null,  null
		}, 3, 3, RANDOM, 4);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		silicon, silicon, silicon,
		silicon, ww_wire, silicon,
		silicon, silicon, silicon
		}, 3, 3, URANDOM, 4);
		
		//Machine parts
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		steel, iron, steel,
		iron,  null, iron,
		steel, iron, steel
		}, 3, 3, frame1);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		null,  null,  steel,
		null,  steel, null,
		steel, null,  null
		}, 3, 3, rod1);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		nuggetSteel,  nuggetSteel,  nuggetSteel,
		nuggetSteel,  null,         nuggetSteel,
		nuggetSteel,  nuggetSteel,  nuggetSteel,
		}, 3, 3, bearing1);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
		wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
		wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
		}, 3, 3, motor1);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.nugget,      rudimentary.base,  rudimentary.nugget,
		wireRudimentary.medium,  rudimentary.base,  wireRudimentary.medium,
		rudimentary.nugget,      rudimentary.base,  rudimentary.nugget
		}, 3, 3, motor1);
		
		//Electric furnaces
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		stone, stone, stone,
		stone, null,  stone,
		stone, stone, stone,
		}, 3, 3, FURNACE);
		
		Craftings.smelting.add(rudimentary.base, wireRudimentary.medium, VoltageTier.V1, 80_000);
		
		//Electric machines - ULV
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.medium, wireRudimentary.medium, wireRudimentary.medium,
		wireRudimentary.medium, FURNACE,                wireRudimentary.medium,
		wireRudimentary.medium, wireRudimentary.medium, wireRudimentary.medium,
		}, 3, 3, COALGEN1);
		ItemEntry efurnace0 = efurnace.blocks.get(0);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base,       coal.base, rudimentary.base,
		wireRudimentary.medium, FURNACE,   wireRudimentary.medium,
		rudimentary.base,       coal.base, rudimentary.base,
		}, 3, 3, efurnace0);
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		stone,                  wireRudimentary.medium, stone,
		wireRudimentary.medium, FURNACE,                wireRudimentary.medium,
		stone,                  wireRudimentary.medium, stone,
		}, 3, 3, crusher.blocks.get(0));
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		stone,     wireRudimentary.medium, stone,
		efurnace0, FURNACE,                efurnace0,
		stone,     wireRudimentary.medium, stone,
		}, 3, 3, alloyer.blocks.get(0));
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		motor1,                  wireRudimentary.medium, motor1,
		wireRudimentary.medium,  rudimentary.gear,       wireRudimentary.medium,
		motor1,                  wireRudimentary.medium, motor1,
		}, 3, 3, cmill.blocks.get(0)); //Cluster Mill ULV
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		motor1,    motor1,                 motor1,
		rudimentary.gear, rudimentary.panel,     rudimentary.gear,
		motor1,    wireRudimentary.medium, motor1,
		}, 3, 3, wiremill.blocks.get(0)); //Wiremill ULV
		//Agriculture
		Craftings.crafting.addRecipeGrid(new ItemEntry[]{
		leaves, leaves, leaves,
		plank,  plank,  plank,
		logs,   logs,   logs
		}, 3, 3, AGRO_TREE);
	}
	
	public static void ingotNugget(Item ingot, Item nugget) {
		Craftings.crafting.addRecipeGrid(nugget, 4, 4, ingot, 1);
		Craftings.crafting.addRecipeGrid(ingot, 1, 1, nugget, 16);
	}
	
	static Block initCraftingType(int tier) {
		int increased = tier + 1;
		String textureName = "machine/assembly "+increased+".png";
		String idName = "crafting."+increased;
		String title = "Crafter Mk"+increased;
		
		Block type = new Crafting(tier);
		type.title(title);
		type.texture(textureName);
		type.register(idName);
		return type;
	}
	
	private CraftGUI component;
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
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
