/**
 * 
 */
package mmb.WORLD.tileworld.tool;

import java.awt.Point;
import java.io.IOException;

import javax.swing.SwingUtilities;

import mmb.DATA.contents.texture.Textures;
import mmb.DATA.file.FileGetter;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.DrawerImage;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ToolCopyPaste implements BlockTool {
	private ToolProxy tproxy;
	public BlockDrawer texture;
	private final Debugger debug = new Debugger("COPY/PASTE");
	/**
	 * 
	 */
	public ToolCopyPaste() {
		texture = new DrawerImage(Textures.get("copy.png"));
	}

	@Override
	public void update(ToolEvent e) {
		// TODO Auto-generated method stub
		BlockTool.super.update(e);
	}

	@Override
	public void mousePress(ToolEvent e, int button) {
		debug.printl("Button #"+button);
		if(e.mouse.getButton() == 1) {
			tproxy.selectionA = e.blockPosition;
			debug.printl("First position: "+e.blockPosition.toString());
		}
		if(e.mouse.getButton() == 2) {
			tproxy.selectionB = e.blockPosition;
			debug.printl("Second position: "+e.blockPosition.toString());
		}
		if(e.mouse.getButton() == 3) {
			int x1 = tproxy.selectionA.x;
			int y1 = tproxy.selectionA.y;
			int x2 = tproxy.selectionB.x;
			int y2 = tproxy.selectionB.y;
			int xn = e.blockPosition.x;
			int yn = e.blockPosition.y;
			
			if(x1 > x2) {
				int z = x1;
				x1 = x2;
				x2 = z;
			}
			if(y1 > y2) {
				int z = y1;
				y1 = y2;
				y2 = z;
			}
			
			int i = 0;
			int X = x2 - x1, Y = y2 - y1;
			for(int x = 0; x < X; x++) {
				for(int y = 0; y < Y; y++) {
					MapEntry b = e.proxy.get(x+x1, y+y1);
					b = b.deepClone();
					i++;
					e.proxy.set(b, new Point(x+xn, y+yn));
				}
			}
		}
	}

	@Override
	public void setProxy(ToolProxy proxy) {
		tproxy = proxy;
	}

	@Override
	public BlockDrawer texture() {
		return texture;
	}

}
