/**
 * 
 */
package mmbtest;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.annotations.Nil;
import mmb.engine.block.Block;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockEntry;
import mmb.engine.recipe.SimpleItemList;
import mmb.engine.texture.BlockDrawer;

/**
 *
 * @author oskar
 *
 */
public class StandardTestReferences {
	public static final BlockDrawer drawer = new BlockDrawer() {
		@Override
		public void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int w, int h) {
			//dummy
		}
		@Override
		public Icon toIcon() {
			//dummy
			return new ImageIcon();
		}
		@Override
		public int LOD() {
			// dummy
			return 0;
		}
	};
	public static final Block block = createBlock();
	private static Block createBlock() {
		Block blk = new Block()
				.texture(drawer);
		blk.leaveBehind(blk);
		return blk.finish("THIS IS A TEST BLOCK DONT SAVE IT");
	}
	public static final BlockEntityType bet = new BlockEntityType().leaveBehind(block).factory(() -> block).finish("THIS IS A TEST BLOCKENT DONT SAVE IT");
	public static final SimpleItemList ilist = new SimpleItemList(bet, block);
}
