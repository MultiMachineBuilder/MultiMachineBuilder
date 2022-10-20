/**
 * 
 */
package mmbmods.stn;

import java.awt.Color;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.debug.Debugger;
import mmb.world.block.BlockEntityType;
import mmb.world.contentgen.Materials;
import mmb.world.contentgen.MetalGroup;
import mmb.world.electric.VoltageTier;
import mmb.world.item.ItemEntityType;
import mmb.world.item.ItemRaw;
import mmb.world.item.Items;
import mmb.world.items.ItemEntry;
import mmb.world.recipes.CraftingGroups;
import mmb.world.worlds.DataLayers;
import mmb.world.worlds.world.World;
import mmbmods.stn.block.STNStorageAttachment;
import mmbmods.stn.block.STNTerminal;
import mmbmods.stn.network.DataLayerSTN;
import monniasza.collects.datalayer.IndexedDatalayerMap;

/**
 * @author oskar
 *
 */
public class STN {
	private STN() {}
	@Nonnull private static final Debugger debug = new Debugger("STN");
		
	static {
		GlobalSettings.injectResources(ResourceBundle.getBundle("mmbmods.stn.bundle"));
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
	@Nonnull public static final ItemEntityType STN_cabler = new ItemEntityType()
			.title("#STN-cabler")
			.texture("stn/cabler.png")
			.describe("#STN-cabler0")
			.factory(CablingTool::new)
			.finish("stn.cabler");
	
	
	//Blocks
	/** STN Storage Connector */
	@Nonnull public  static final BlockEntityType STN_storage = new BlockEntityType()
		.texture("stn/storage.png")
		.title("#STN-storage")
		.factory(STNStorageAttachment::new)
		.finish("stnb.storage");
	/** STN Terminal */
	@Nonnull public static final BlockEntityType STN_terminal = new BlockEntityType()
		.texture("stn/terminal.png")
		.title("#STN-terminal")
		.factory(STNTerminal::new)
		.finish("stnb.terminal");
	
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
		
		Items.tagItems("STN", STN_cabler, STN_storage, STN_terminal);
		
		Materials.alloying(STN_a, 5, VoltageTier.V2, 800_000, 
			Materials.iron.stack(1),
			Materials.redstone.stack(1),
			Materials.copper.stack(1),
			Materials.lead.stack(1),
			Materials.tin.stack(1));
		Materials.alloying(STN_b, 4, VoltageTier.V2, 800_000, 
			Materials.silver.stack(1),
			Materials.cobalt.stack(1),
			Materials.glowstone.stack(1),
			STN_a.stack(1));
		
		CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
		null, STN_a.frag, STN_a.rod,
		null, STN_a.rod, STN_a.frag,
		STN_a.rod, null, null
		}, 3, 3, ItemRaw.make(STN_cabler));
	}
	
	
}
