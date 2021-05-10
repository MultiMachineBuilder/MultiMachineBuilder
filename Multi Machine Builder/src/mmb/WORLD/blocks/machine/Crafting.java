/**
 * 
 */
package mmb.WORLD.blocks.machine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockFactory;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class Crafting extends SkeletalBlockEntityData {
	public final int size;
	public final int tier;
	private final BlockEntityType type;
	public Crafting(int x, int y, BlockMap owner2, int tier) {
		super(x, y, owner2);
		size = sizes[tier];
		this.tier = tier;
		setupRefsTable();
		type = types[tier];
	}

	@Override
	public BlockType type() {
		return type;
	}

	@Override
	public void load(JsonNode data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void save0(ObjectNode node) {
		// TODO Auto-generated method stub

	}

	
	//Tiers
	static int[] sizes = {2, 3, 4, 5, 7, 9};
	static BlockEntityType[] types; //initialized below
	public static void setupRefsTable() {
		if(types == null) {
			int n = 6;
			types = new BlockEntityType[n];
			for(int i = 0; i < n; i++) {
				types[i] = initCraftingType(i);
			}
		}
	}
	static BlockEntityType initCraftingType(int tier) {
		BlockFactory factory = (x, y, m) -> new Crafting(x, y, m, tier);
		int increased = tier + 1;
		String textureName = "machine/assembly "+increased+".png";
		String idName = "crafting."+increased;
		BlockDrawer drawer = BlockDrawer.ofImage(Textures.get(textureName));
		String title = "Crafter Mk"+increased;
		
		
		BlockEntityType type = new BlockEntityType();
		type.title = title;
		type.drawer = drawer;
		type.setFactory(factory);
		type.register(idName);
		return type;
	}
}
