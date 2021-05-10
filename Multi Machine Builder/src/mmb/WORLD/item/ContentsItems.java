/**
 * 
 */
package mmb.WORLD.item;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	public static final Item copper, iron, silicon, leaf, coal;
	
	static {
		copper = new Item();
		copper.title = "Copper Ingot";
		copper.drawer = BlockDrawer.ofImage(Textures.get("item/copper ingot.png"));
		copper.volume = 0.00125;
		copper.register("ingot.copper");
		
		iron = new Item();
		iron.title = "Iron Ingot";
		iron.drawer = BlockDrawer.ofImage(Textures.get("item/iron ingot.png"));
		iron.volume = 0.00125;
		iron.register("ingot.iron");
		
		silicon = new Item();
		silicon.title = "Silicon Ingot";
		silicon.drawer = BlockDrawer.ofImage(Textures.get("item/silicon ingot.png"));
		silicon.volume = 0.00125;
		silicon.register("ingot.silicon");
		
		leaf = new Item();
		leaf.title = "Leaf";
		leaf.drawer = BlockDrawer.ofImage(Textures.get("item/lisc.png"));
		leaf.volume = 0.000125;
		leaf.register("plant.leaf");
		
		coal = new Item();
		coal.title = "Coal";
		coal.drawer = BlockDrawer.ofImage(Textures.get("item/coal.png"));
		coal.volume = 0.00125;
		coal.register("gem.coal");
	}
}
