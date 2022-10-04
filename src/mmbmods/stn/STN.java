/**
 * 
 */
package mmbmods.stn;

import java.awt.Color;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.contentgen.MetalGroup;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.item.ItemRaw;
import mmb.WORLD.item.Items;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.recipes.CraftingGroups;
import mmb.WORLD.worlds.DataLayers;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.datalayer.IndexedDatalayerMap;

/**
 * @author oskar
 *
 */
public class STN {
	private STN() {}
	@Nonnull private static final Debugger debug = new Debugger("STN");
	
	static {
		debug.printl("Loading resources");
		GlobalSettings.injectResources(ResourceBundle.getBundle("mmbmods.stn.bundle", GlobalSettings.locale()));
		debug.printl("Resources loaded");
		
		STN_cabler = new ItemEntityType()
				.title("#STN-cabler")
				.texture("stn/cabler.png")
				.describe("#STN-cabler0")
				.factory(CablingTool::new)
				.finish("stn.cabler");
		
		GlobalSettings.dumpBundle(GlobalSettings.bundle());
	}
	
	//Materials
	@Nonnull public static final MetalGroup STN_a =  new MetalGroup(new Color(0,  255, 160), "stna", VoltageTier.V2,  100_000, false);
	@Nonnull public static final MetalGroup STN_b =  new MetalGroup(new Color(90, 255, 160), "stnb", VoltageTier.V3,  400_000, false);
	@Nonnull public static final MetalGroup STN_c =  new MetalGroup(new Color(90, 255, 250), "stnc", VoltageTier.V4, 1600_000, false);
	@Nonnull public static final MetalGroup STN_d =  new MetalGroup(new Color(0,  255, 250), "stnd", VoltageTier.V5, 6400_000, false);
	
	//Items
	/**
	 * Allows to add or remove STN cables
	 */
	@Nonnull public static final ItemEntityType STN_cabler;
	
	//The data layer
	/**
	 * The main data layer for STN
	 */
	@Nonnull public static final IndexedDatalayerMap<World, DataLayerSTN> STN_datalayer //NOSONAR this is a data layer
	= DataLayers.createWorldDataLayerUsingNode("STN", DataLayerSTN::new);
	
	private static boolean inited = false;
	/** Initializes the STN */
	public static void init() {
		debug.printl("Initing");
		if(inited) return;
		inited = true;
		
		Items.tagItems("STN", STN_cabler);
		
		Materials.alloying(STN_a, 5, VoltageTier.V2, 800_000, 
			Materials.iron.stack(1),
			Materials.redstone.stack(1),
			Materials.copper.stack(1),
			Materials.lead.stack(1),
			Materials.tin.stack(1));
		CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
		null, STN_a.frag, STN_a.rod,
		null, STN_a.rod, STN_a.frag,
		STN_a.rod, null, null
		}, 3, 3, ItemRaw.make(STN_cabler));
	}
	
	
}
