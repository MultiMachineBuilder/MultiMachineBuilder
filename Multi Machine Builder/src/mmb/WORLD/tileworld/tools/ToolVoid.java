/**
 * 
 */
package mmb.WORLD.tileworld.tools;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.DrawerImage;
import mmb.WORLD.tileworld.tool.BlockTool;
import mmb.WORLD.tileworld.tool.ToolEvent;
import mmb.WORLD.tileworld.tool.ToolProxy;

/**
 * @author oskar
 *
 */
public class ToolVoid implements BlockTool {
	private BlockDrawer draw = new DrawerImage(Textures.get("highways.png"));
	@Override
	public void mousePress(ToolEvent e, int button) {
		mouseDrag(e);
	}

	@Override
	public void mouseDrag(ToolEvent e) {
		e.proxy.set(null, e.blockPosition);
	}

	@Override
	public void setProxy(ToolProxy proxy) {}

	@Override
	public BlockDrawer texture() {
		return draw;
	}

}
