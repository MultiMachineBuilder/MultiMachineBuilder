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
	
	//Minerals
	@Nonnull public static final Item leaf = new Item()
			.title("Leaf")
			.texture("item/lisc.png")
			.volumed(0.000125)
			.finish("plant.leaf");
	/*@Nonnull public static final Item coal = new Item()
			.title("Coal")
			.texture("item/coal.png")
			.volumed(0.00125)
			.finish("gem.coal");*/
	/*@Nonnull public static final Item diamond = new Item()
			.title("Diamond")
			.texture("item/diamond.png")
			.volumed(0.00125)
			.finish("gem.diamond");*/
	
	//Pickaxes
	@Nonnull public static final PickaxeType pickVW = Pickaxe.create(5, "item/wood pick.png", "Very weak pickaxe", "pick.weak");
	@Nonnull public static final PickaxeType pickWood = Pickaxe.create(100, "item/wood pick.png", "Wooden pickaxe", "pick.wood");

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

	//Packaged items
	@Nonnull public static final ItemEntityType pack = new ItemEntityType()
			.title("Simple Item Package")
			.texture("item/package.png")
			.volumed(0.001)
			.factory(Pack::createEmpty)
			.finish("boxed.packItem");
}
