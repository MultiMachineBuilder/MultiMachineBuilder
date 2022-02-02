/**
 * 
 */
package mmb.WORLD.items;

import javax.annotation.Nonnull;

import mmb.WORLD.blocks.machine.pack.Pack;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.data.ItemBOM;
import mmb.WORLD.items.data.Stencil;
import mmb.WORLD.items.pickaxe.Pickaxe;
import mmb.WORLD.items.pickaxe.Pickaxe.PickaxeType;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	
	/** Initializes items */
	public static void init() {}
		
	//Pickaxe heads
	@Nonnull public static final Item pickHeadWood = new Item()
			.title("Wooden pickaxe head")
			.texture("item/wood pick head.png")
			.volumed(0.00125)
			.finish("pickHead.wood");
	@Nonnull public static final Item pickHeadRudimentary = new Item()
			.title("Rudimentary pickaxe head")
			.texture("item/rudimentary pick head.png")
			.volumed(0.00125)
			.finish("pickHead.rudimentary");
	//Minerals
	@Nonnull public static final Item leaf = new Item()
			.title("Leaf")
			.texture("item/lisc.png")
			.volumed(0.000125)
			.finish("plant.leaf");
	
	//Pickaxes
	@Nonnull public static final PickaxeType pickVW = Pickaxe.create(120, 15, "item/wood pick.png", "Very weak pickaxe", "pick.weak");
	@Nonnull public static final PickaxeType pickWood = Pickaxe.create(100, 100, "item/wood pick.png", "Wooden pickaxe", "pick.wood");
	@Nonnull public static final PickaxeType pickRudimentary = Pickaxe.create(50, 400, "item/rudimentary pick.png", "Rudimentary pickaxe", "pick.rudimentary");
	@Nonnull public static final Item bucket = new Bucket()
			.title("Item Bucket")
			.texture("dropItems.png")
			.finish("mmb.bucket");
	//Crafting aids
	@Nonnull public static final ItemEntityType stencil = new ItemEntityType()
			.title("Crafting stencil")
			.texture("item/stencil.png")
			.volumed(0.001)
			.factory(Stencil::new)
			.finish("crafting.cstencil");
	@Nonnull public static final ItemEntityType BOM = new ItemEntityType()
			.title("Bill Of Materials")
			.texture("item/list.png")
			.volumed(0.001)
			.factory(ItemBOM::new)
			.finish("crafting.BOMItems");
	
	//Machine parts
	@Nonnull public static final Item frame1 = new Item()
		.title("Machine Frame #1")
		.texture("item/frame 1.png")
		.finish("industry.frame1");
	@Nonnull public static final Item rod1 = new Item()
		.title("Machine Rod #1")
		.texture("item/steel rod.png")
		.volumed(0.00125)
		.finish("industry.rod1");
	@Nonnull public static final Item bearing1 = new Item()
			.title("Machine Bearing #1")
			.texture("item/ring 1.png")
			.volumed(0.00125)
			.finish("industry.bearing1");
	@Nonnull public static final Item motor1 = new Item()
			.title("Machine Motor #1")
			.texture("item/motor 1.png")
			.volumed(0.00125)
			.finish("industry.motor1");

	//Packaged items
	@Nonnull public static final ItemEntityType pack = new ItemEntityType()
			.title("Simple Item Package")
			.texture("item/package.png")
			.volumed(0.001)
			.factory(Pack::createEmpty)
			.finish("boxed.packItem");
}
