/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.image.BufferedImage;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Rotation;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ItemTransporter extends SkeletalBlockEntityRotary {

	public static final BufferedImage TEXTURE = Textures.get("machine/imover.png");
	public static final RotatedImageGroup RTEXTURE = RotatedImageGroup.create(TEXTURE);
	private static final Debugger debug = new Debugger("ITEM TRANSPORT");
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
		moveItems(x, y, map, side);
	}
	
	public static void moveItems(int x, int y, MapProxy map, Rotation side) {
		Side d = side.D();
		Side u = side.U();
		
		InventoryReader srcr = map.getAtSide(x, y, d).getOutput(u);
		InventoryWriter dstw = map.getAtSide(x, y, u).getInput(d);
		
		while(srcr.hasCurrent() || srcr.hasNext()) {
			if(srcr.hasCurrent()) {
				//debug.printl("["+x+","+y+"]");
				//debug.printl("Item found");
				int tbread = srcr.toBeExtracted(1);
				int tbwrite = dstw.toBeWritten(srcr.currentItem(), 1);
				//debug.printl(tbread + " ==> " + tbwrite);
				if(tbread > 0 && tbwrite > 0) {
					ItemEntry curr = srcr.currentItem();
					srcr.extract(1);
					dstw.write(curr);
					return;
				}
				if(tbwrite == 0) {
					//Skip
					if(srcr.level().level > 0 && srcr.hasNext()) srcr.skip();
					else return;
				}
			}else if(srcr.hasNext()) {
				srcr.next();
			}
		}	
	}
}
