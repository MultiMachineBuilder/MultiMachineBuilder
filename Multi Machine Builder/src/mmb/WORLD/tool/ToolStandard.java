/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.gui.window.WorldFrame;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ToolStandard extends WindowTool{
	@Override
	public void mousePressed(MouseEvent e) {
		int x = frame.getMouseoverBlockX();
		int y = frame.getMouseoverBlockY();
		debug.printl("Click");
		switch(e.getButton()) {
		case 0:
			break;
		case 1: //LMB
			//If the block has ActionListener, run the listener
			World map = frame.getMap();
			if(!map.inBounds(x, y)) break;
			BlockEntry ent = map.get(x, y);
			if(ent instanceof BlockActivateListener) {
				((BlockActivateListener) ent).click(x, y, map, window);
				debug.printl("Running BlockActivateListener for: ["+x+","+y+"]");
			}else {
				Placer placer0 = window.getPlacer().getSelectedValue();
				if(placer0 == null) return;
				placer0.place(x, y, map);
			}
			break;
		case 3: //RMB
			frame.showPopup(e);
			break;
		case 2: //MMB
			break;
		default:
			break;
		}
	}
	private WorldFrame frame;
	@Override
	public void setWindow(WorldWindow window) {
		this.window = window;
		frame = window.getWorldFrame();
	}
	private static final Debugger debug = new Debugger("TOOL-STANDARD");
	public ToolStandard() {
		super("standard");
	}
	public static final Icon ICON_NORMAL = new ImageIcon(Textures.get("tool/normal.png"));
	@Override
	public Icon getIcon() {
		return ICON_NORMAL;
	}
	@Override
	public String title() {
		return "Standard Tool";
	}

}
