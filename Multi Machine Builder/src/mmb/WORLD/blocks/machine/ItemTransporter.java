/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.image.BufferedImage;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.inventory.Inventories;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class ItemTransporter extends SkeletalBlockEntityRotary {

	public static final BufferedImage TEXTURE = Textures.get("machine/imover.png");
	public static final RotatedImageGroup RTEXTURE = RotatedImageGroup.create(TEXTURE);
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	public ItemTransporter(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.IMOVER;
	}

	@Override
	public RotatedImageGroup getImage() {
		return RTEXTURE;
	}

	@Override
	public void onTick(MapProxy map) {
		Side d = side.D();
		Side u = side.U();
		Inventory src = map.getAtSide(x, y, d).getInventory(u);
		Inventory dst = map.getAtSide(x, y, u).getInventory(d);
		
		for(ItemRecord item: src) {
			int transferred = Inventories.transfer(item, dst, 1);
			if(transferred == 1) return;
		}
		
	}
	

}
