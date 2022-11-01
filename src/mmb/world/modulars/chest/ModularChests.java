/**
 * 
 */
package mmb.world.modulars.chest;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.world.block.BlockEntityType;
import mmb.world.item.Items;

/**
 * Items and blocks for modular chests
 * @author oskar
 */
public class ModularChests {
	private ModularChests() {}
	/** Initializes the modular chests*/
	public static void init() {
		//empty
	}
	
	//Translation keys
	@Nonnull private static final String s_import = GlobalSettings.$res("modchest-import");
	@Nonnull private static final String s_export = GlobalSettings.$res("modchest-export");
	
	//The modular chest itself
	/** The modular chest body */
	@Nonnull public static final BlockEntityType chest = new BlockEntityType()
		.texture("modules/chest_body.png")
		.title("#modchest-chest")
		.factory(ModularChest::new)
		.finish("modchest.chest");
	
	//Modules given to the player
	
	
	
	static {
		Items.tagItems("modular", chest);
	}
}
