/**
 * 
 */
package mmb.content.stn;

import java.awt.Color;
import java.util.ResourceBundle;

import mmb.NN;
import mmb.content.CraftingGroups;
import mmb.content.electric.VoltageTier;
import mmb.content.rawmats.Materials;
import mmb.content.rawmats.MetalGroup;
import mmb.content.stn.block.STNExporter;
import mmb.content.stn.block.STNFramework;
import mmb.content.stn.block.STNImporter;
import mmb.content.stn.block.STNStorageAttachment;
import mmb.content.stn.block.STNTerminal;
import mmb.content.stn.network.DataLayerSTN;
import mmb.engine.block.BlockEntityType;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemRaw;
import mmb.engine.item.Items;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.worlds.DataLayers;
import mmb.engine.worlds.world.World;
import monniasza.collects.datalayer.IndexedDatalayerMap;

/**
 * @author oskar
 *
 */
public class STN {
	private STN() {}
	@NN private static final Debugger debug = new Debugger("STN");
		
	static {
		GlobalSettings.injectResources(ResourceBundle.getBundle("mmb.content.stn.bundle"));
	}
	//Materials
	@NN public static final MetalGroup STN_a =  new MetalGroup(new Color(0,  255, 160), "stna", VoltageTier.V2,  100_000, false);
	@NN public static final MetalGroup STN_b =  new MetalGroup(new Color(90, 255, 160), "stnb", VoltageTier.V3,  400_000, false);
	@NN public static final MetalGroup STN_c =  new MetalGroup(new Color(90, 255, 250), "stnc", VoltageTier.V4, 1600_000, false);
	@NN public static final MetalGroup STN_d =  new MetalGroup(new Color(0,  255, 250), "stnd", VoltageTier.V5, 6400_000, false);
	
	//Items
	/** Allows to add or remove STN cables */
	@NN public static final ItemEntityType STN_cabler = new ItemEntityType()
		.title("#STN-cabler")
		.texture("stn/cabler.png")
		.describe("#STN-cabler0")
		.factory(CablingTool::new)
		.finish("stn.cabler");
	
	//Blocks
	/** STN Storage Connector */
	@NN public  static final BlockEntityType STN_storage = new BlockEntityType()
		.texture("stn/storage.png")
		.title("#STN-storage")
		.factory(STNStorageAttachment::new)
		.finish("stnb.storage");
	/** STN Terminal */
	@NN public static final BlockEntityType STN_terminal = new BlockEntityType()
		.texture("stn/terminal.png")
		.title("#STN-terminal")
		.factory(STNTerminal::new)
		.finish("stnb.terminal");
	/** STN Exporter */
	@NN public static final BlockEntityType STN_exporter = new BlockEntityType()
		.texture("stn/exporter.png")
		.title("#STN-exporter")
		.factory(STNExporter::new)
		.finish("stnb.exporter");
	/** STN Importer */
	@NN public static final BlockEntityType STN_importer= new BlockEntityType()
		.texture("stn/importer.png")
		.title("#STN-importer")
		.factory(STNImporter::new)
		.finish("stnb.importer");
	/** STN Framework */
	@NN public static final BlockEntityType STN_fw= new BlockEntityType()
		.texture("stn/fw.png")
		.title("#STN-framework")
		.factory(STNFramework::new)
		.finish("stnb.framework");
	
	//The data layer
	/**
	 * The main data layer for STN
	 */
	@NN public static final IndexedDatalayerMap<@NN World, DataLayerSTN> STN_datalayer //NOSONAR this is a data layer
	= DataLayers.createWorldDataLayerUsingNode("STN", DataLayerSTN::new);
	
	private static boolean inited = false;
	/** Initializes the STN */
	public static void init() {
		debug.printl("Initing");
		if(inited) return;
		inited = true;
		
		Items.tagItems("STN", STN_cabler, STN_storage, STN_terminal, STN_exporter, STN_importer, STN_fw);
		
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
