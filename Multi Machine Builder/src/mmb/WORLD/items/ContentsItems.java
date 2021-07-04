/**
 * 
 */
package mmb.WORLD.items;

import mmb.WORLD.item.Item;
import mmb.WORLD.item.ItemEntityType;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	public static final Item copper = new Item()
			.title("Copper ingot")
			.texture("item/copper ingot.png")
			.volumed(0.00125)
			.finish("ingot.copper");
	public static final Item iron = new Item()
			.title("Iron ingot")
			.texture("item/iron ingot.png")
			.volumed(0.00125)
			.finish("ingot.iron");
	public static final Item silicon = new Item()
			.title("Silicon ingot")
			.texture("item/silicon ingot.png")
			.volumed(0.00125)
			.finish("ingot.silicon");
	public static final Item leaf = new Item()
			.title("Leaf")
			.texture("item/lisc.png")
			.volumed(0.000125)
			.finish("plant.leaf");
	public static final Item coal = new Item()
			.title("Coal")
			.texture("item/coal.png")
			.volumed(0.00125)
			.finish("gem.coal");
	public static final Item gold = new Item()
			.title("Gold ingot")
			.texture("item/gold ingot.png")
			.volumed(0.00125)
			.finish("ingot.gold");
	public static final Item uranium = new Item()
			.title("Uranium ingot")
			.texture("item/uranium ingot.png")
			.volumed(0.00125)
			.finish("ingot.uranium");
	public static final ItemEntityType stencil = new ItemEntityType()
			.title("Crafting stencil")
			.texture("item/stencil.png")
			.volumed(0.001)
			.factory(Stencil::new)
			.finish("crafting.cstencil");
	public static final Item silver = new Item()
			.title("Silver ingot")
			.texture("item/silver ingot.png")
			.volumed(0.00125)
			.finish("ingot.silver");
	public static final Item diamond = new Item()
			.title("Diamond")
			.texture("item/diamond.png")
			.volumed(0.00125)
			.finish("gem.coal");
}
