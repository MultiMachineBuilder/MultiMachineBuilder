/**
 * 
 */
package mmb.WORLD.tileworld.tools;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.DrawerImage;
import mmb.WORLD.tileworld.block.BlockEntity;
import mmb.WORLD.tileworld.block.BlockEntityData;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.tool.BlockTool;
import mmb.WORLD.tileworld.tool.ToolEvent;
import mmb.WORLD.tileworld.tool.ToolProxy;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ToolTextEditor implements BlockTool {
	private static final BlockDrawer texture;
	private static final Debugger debug = new Debugger("TEXT EDITOR TOOL");
	/**
	 * 
	 */
	static{
		texture = new DrawerImage(Textures.get("textedit.png"));
	}
	public ToolTextEditor() {}

	@Override
	public void mousePress(ToolEvent e, int button) {
		MapEntry me = e.proxy.get(e.blockPosition);
		if(me == null) {
			debug.printl(e.blockPosition.toString() +" is a void and can't be edited.");
			return;
		}
		//debug.printl(me.getClass().getCanonicalName());
		if(me instanceof BlockEntity) {
			BlockEntity be = (BlockEntity) me;
			BlockEntityData bed = be.bed;
			if(bed == null) {
				debug.printl(e.blockPosition.toString() +" is badly initialized and can't be edited.");
				return;
			}
			if(!(bed instanceof Blocks.BlockDataText)) {
				debug.printl(e.blockPosition.toString() +" is not a text block and can't be edited.");
				return;
			}
			Blocks.BlockDataText bdt = (Blocks.BlockDataText) bed;
			new TextEditor(bdt).setVisible(true);
		}else {
			debug.printl(e.blockPosition.toString() +" is not a block entity and can't be edited.");
			return;
		}

	}

	@Override
	public void setProxy(ToolProxy proxy) {}

	@Override
	public BlockDrawer texture() {
		return texture;
	}

}
