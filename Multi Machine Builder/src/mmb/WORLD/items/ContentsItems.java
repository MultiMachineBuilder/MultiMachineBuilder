/**
 * 
 */
package mmb.WORLD.items;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	public static final ItemType copper, iron, silicon, leaf;
	
	static {
		copper = new ItemType();
		copper.id = "ingot.copper";
		copper.title = "Copper Ingot";
		copper.drawer = BlockDrawer.ofImage(Textures.get("item/copper_ingot.png"));
		copper.register();
		
		iron = new ItemType();
		iron.id = "ingot.iron";
		iron.title = "Iron Ingot";
		iron.drawer = BlockDrawer.ofImage(Textures.get("item/iron_ingot.png"));
		iron.register();
		
		silicon = new ItemType();
		silicon.id = "ingot.silicon";
		silicon.title = "Silicon Ingot";
		silicon.drawer = BlockDrawer.ofImage(Textures.get("item/silicon_ingot.png"));
		silicon.register();
		
		leaf = new ItemType();
		leaf.id = "plant.leaf";
		leaf.title = "Leaf";
		leaf.drawer = BlockDrawer.ofImage(Textures.get("item/lisc.png"));
		leaf.register();
	}
}
