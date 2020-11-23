/**
 * 
 */
package mmb.WORLD.inventory.items;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.inventory.ItemType;
import mmb.WORLD.inventory.SimpleItem;
import mmb.WORLD.tileworld.DrawerImage;

/**
 * @author oskar
 *
 */
public class Items {
	public static HashMap<String, ItemType> items = new HashMap<String, ItemType>();
	public static final SimpleItem copper, iron, silicon, leaf;
	static {
		copper = new SimpleItem();
		copper.id = "mmbi.copper";
		copper.name = "Copper ingot";
		copper.texture = new DrawerImage(Textures.get("item/copper_ingot.png"));
		copper.register();
		
		iron = new SimpleItem();
		iron.id = "mmbi.iron";
		iron.name = "Iron ingot";
		iron.texture = new DrawerImage(Textures.get("item/iron_ingot.png"));
		iron.register();
		
		silicon = new SimpleItem();
		silicon.id = "mmbi.silicon";
		silicon.name = "Silicon ingot";
		silicon.texture = new DrawerImage(Textures.get("item/silicon_ingot.png"));
		silicon.register();
		
		leaf = new SimpleItem();
		leaf.id = "mmbi.leaf";
		leaf.name = "Leaf";
		leaf.texture = new DrawerImage(Textures.get("item/lisc.png"));
		leaf.volume = 0.0001;
		leaf.register();
	}
	
	public static ItemType[] getItems() {
		String[] keys = items.keySet().toArray(new String[items.size()]);
		ItemType[] result = new ItemType[items.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = items.get(keys[i]);
		}
		return result;
	}
}
