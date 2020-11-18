/**
 * 
 */
package mmb.WORLD.tileworld.tools;

import java.awt.Color;
import java.io.IOException;

import mmb.DATA.contents.texture.Textures;
import mmb.DATA.file.FileGetter;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.DrawerImage;
import mmb.WORLD.tileworld.DrawerPlainColor;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.tool.BlockTool;
import mmb.WORLD.tileworld.tool.ToolEvent;
import mmb.WORLD.tileworld.tool.ToolProxy;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ToolPlace implements BlockTool {
	private final boolean isBrush;
	private static final BlockDrawer iBrush;
    private static final BlockDrawer iPlace;
	private final BlockDrawer texture;
	private static final Debugger debug = new Debugger("TOOL-PLACE");
	/**
	 * 
	 */
	static {
		iBrush = new DrawerImage(Textures.get("brush.png"));
		iPlace = new DrawerImage(Textures.get("place.png"));
	}
	public ToolPlace(boolean brush) {
		isBrush = brush;
		texture = brush?iBrush:iPlace;
	}

	@Override
	public void mousePress(ToolEvent e, int button) {
		e.proxy.set(MapEntry.createNew(e.selectedBlock), e.blockPosition);
	}

	@Override
	public void mouseDrag(ToolEvent e) {
		if(isBrush) {
			e.proxy.set(MapEntry.createNew(e.selectedBlock), e.blockPosition);
		}
	}

	@Override
	public void setProxy(ToolProxy proxy) {}

	@Override
	public BlockDrawer texture() {
		return texture;
	}

}
