/**
 * 
 */
package mmb.WORLD.items;

import javax.annotation.Nonnull;

import mmb.WORLD.item.Item;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.pickaxe.Pickaxe;
import mmb.WORLD.items.pickaxe.Pickaxe.PickaxeType;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	/** Initializes items */
	public static void init() {}
	
	//Metal ingots
	@Nonnull public static final Item copper = new Item()
			.title("Copper ingot")
			.texture("item/copper ingot.png")
			.volumed(0.00125)
			.finish("ingot.copper");
	@Nonnull public static final Item iron = new Item()
			.title("Iron ingot")
			.texture("item/iron ingot.png")
			.volumed(0.00125)
			.finish("ingot.iron");
	@Nonnull public static final Item silicon = new Item()
			.title("Silicon ingot")
			.texture("item/silicon ingot.png")
			.volumed(0.00125)
			.finish("ingot.silicon");
	@Nonnull public static final Item gold = new Item()
			.title("Gold ingot")
			.texture("item/gold ingot.png")
			.volumed(0.00125)
			.finish("ingot.gold");
	@Nonnull public static final Item uranium = new Item()
			.title("Uranium ingot")
			.texture("item/uranium ingot.png")
			.volumed(0.00125)
			.finish("ingot.uranium");
	@Nonnull public static final Item silver = new Item()
			.title("Silver ingot")
			.texture("item/silver ingot.png")
			.volumed(0.00125)
			.finish("ingot.silver");
	@Nonnull public static final Item stainless = new Item()
			.title("Stainless steel ingot")
			.texture("item/stainless ingot.png")
			.volumed(0.00125)
			.finish("ingot.stainless");
	@Nonnull public static final Item steel = new Item()
			.title("Steel ingot")
			.texture("item/steel ingot.png")
			.volumed(0.00125)
			.finish("ingot.steel");
	
	//Metal nuggets
	@Nonnull public static final Item nuggetCopper = new Item()
			.title("Copper ingot")
			.texture("item/copper nugget.png")
			.volumed(0.000078125)
			.finish("nugget.copper");
	@Nonnull public static final Item nuggetIron = new Item()
			.title("Iron nugget")
			.texture("item/iron nugget.png")
			.volumed(0.000078125)
			.finish("nugget.iron");
	@Nonnull public static final Item nuggetSilicon = new Item()
			.title("Silicon nugget")
			.texture("item/silicon nugget.png")
			.volumed(0.000078125)
			.finish("nugget.silicon");
	@Nonnull public static final Item nuggetGold = new Item()
			.title("Gold nugget")
			.texture("item/gold nugget.png")
			.volumed(0.000078125)
			.finish("nugget.gold");
	@Nonnull public static final Item nuggetUranium = new Item()
			.title("Uranium nugget")
			.texture("item/uranium nugget.png")
			.volumed(0.000078125)
			.finish("nugget.uranium");
	@Nonnull public static final Item nuggetSilver = new Item()
			.title("Silver nugget")
			.texture("item/silver nugget.png")
			.volumed(0.000078125)
			.finish("nugget.silver");
	@Nonnull public static final Item nuggetStainless = new Item()
			.title("Stainless steel nugget")
			.texture("item/stainless nugget.png")
			.volumed(0.000078125)
			.finish("nugget.stainless");
	@Nonnull public static final Item nuggetSteel = new Item()
			.title("Steel nugget")
			.texture("item/steel nugget.png")
			.volumed(0.000078125)
			.finish("nugget.steel");
	
	//Wire spools
	@Nonnull public static final Item wireAlu = new Item()
			.title("Aluminum wire spool")
			.texture("item/alu wire.png")
			.volumed(0.00125)
			.finish("wirespool.alu");
	@Nonnull public static final Item wireCopper = new Item()
			.title("Copper wire spool")
			.texture("item/copper wire.png")
			.volumed(0.00125)
			.finish("wirespool.copper");
	@Nonnull public static final Item wireGold = new Item()
			.title("Gold wire spool")
			.texture("item/gold wire.png")
			.volumed(0.00125)
			.finish("wirespool.gold");
	@Nonnull public static final Item wireSilver = new Item()
			.title("Silver wire spool")
			.texture("item/silver wire.png")
			.volumed(0.00125)
			.finish("wirespool.silver");
	@Nonnull public static final Item wireStainless = new Item()
			.title("Stainless steel wire spoolt")
			.texture("item/stainless wire.png")
			.volumed(0.00125)
			.finish("wirespool.stainless");
	@Nonnull public static final Item wireSteel = new Item()
			.title("Steel wire spool")
			.texture("item/steel wire.png")
			.volumed(0.00125)
			.finish("wirespool.steel");
		
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
	@Nonnull public static final Item coal = new Item()
			.title("Coal")
			.texture("item/coal.png")
			.volumed(0.00125)
			.finish("gem.coal");
	@Nonnull public static final Item diamond = new Item()
			.title("Diamond")
			.texture("item/diamond.png")
			.volumed(0.00125)
			.finish("gem.diamond");
	
	
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
}
