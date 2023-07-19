/**
 * 
 */
package mmb.menu.wtool;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.joml.Vector2d;

import mmb.NN;
import mmb.cgui.BlockActivateListener;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.Placer;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldPopup;
import mmb.menu.world.window.WorldWindow;

/**
 * This class defines a standard tool, applicable to most items.
 * @author oskar
 */
public class ToolStandard extends WindowTool{
	private static final Debugger debug = new Debugger("TOOL-STANDARD");
	
	/** Creates a standard tool*/
	public ToolStandard() {
		super("standard");
	}
	@Override
	public void preview(int x, int y, double scale, Graphics g) {
		ItemRecord irecord = frame.getPlacer().getSelectedValue();
		if(irecord == null) return;
		ItemEntry placer0 = irecord.item();
		if(placer0 instanceof Placer) ((Placer) placer0).preview(g, new Point(x, y), frame.getMap(), frame.getMouseoverBlock(), (int)Math.ceil(scale));
	}
	
	//Keyboard
	@Override
	public void keyPressed(KeyEvent e) {
		World map = frame.getMap();
		if(!map.inBounds(bx, by)) return;
		BlockEntry ent = map.get(bx, by);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_I:
			ent.flipH();
			debug.printl("Flipped");
			break;
		case KeyEvent.VK_J:
			ent.flipNW();
			debug.printl("Flipped");
			break;
		case KeyEvent.VK_K:
			ent.flipV();
			debug.printl("Flipped");
			break;
		case KeyEvent.VK_L:
			ent.flipNE();
			debug.printl("Flipped");
			break;
		default:
			break;
		}
	}
	//Mouse
	private int bx, by;
	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		Vector2d mouseover = frame.worldAt(mx, my, new Vector2d());
		int x = (int) Math.floor(mouseover.x);
		int y = (int) Math.floor(mouseover.y);
		int b = e.getButton();
		
		if(frame.altPressed()) {
			if(frame.ctrlPressed()) 
				mousePressedAltCtrl(b, x, y);
			else mousePressedAlt(b, x, y);
		}else if(frame.ctrlPressed()) 
			mousePressedCtrl(b, x, y);
		else if(frame.shiftPressed()) 
			mousePressedShift(b, x, y);
		else mousePressedPlain(e, mouseover.x, mouseover.y);
	}
	
	private void mousePressedAlt(int b, int x, int y) {
		switch(b) {
		case 0:
			break;
		case 1: //LMB
			WorldPopup.dropItems(frame.window, x, y, 1);
			break;
		case 3: //RMB
			WorldPopup.pickupItems(frame.window, x, y, true);
			break;
		case 2: //MMB
			break;
		default:
			break;
		}
	}
	private void mousePressedAltCtrl(int b, int x, int y) {
		switch(b) {
		case 0:
			break;
		case 1: //LMB
			WorldPopup.dropItems(frame.window, x, y, Integer.MAX_VALUE);
			break;
		case 3: //RMB
			WorldPopup.pickupItems(frame.window, x, y, false);
			break;
		case 2: //MMB
			break;
		default:
			break;
		}
	}
	private void mousePressedPlain(MouseEvent e, double x0, double y0) {
		int x = (int) Math.floor(x0);
		int y = (int) Math.floor(y0);
		
		switch(e.getButton()) {
		case 0:
			break;
		case 1: //LMB
			//If the block has ActionListener, run the listener
			World map = frame.getMap();
			if(!map.inBounds(x, y)) break;
			BlockEntry ent = map.get(x, y);
			if(ent instanceof BlockActivateListener) {
				((BlockActivateListener) ent).click(x, y, map, window, x0 - x, y0 - y);
				debug.printl("Running BlockActivateListener for: ["+x+","+y+"]");
			}else {
				placeBlock(x, y, map, window);
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
	private void mousePressedCtrl(int b, int x, int y) {
		BlockEntry block = frame.getMap().get(x, y);
		switch(b) {
		case 1: //LMB
			//Turn CCW
			block.ccw();
			break;
		case 2: //MMB
			//Turn around
			block.ccw();
			block.ccw();
			break;
		case 3: //RMB
			//Turn CW
			block.cw();
			break;
		default:
			break;
		}
	}
	private void mousePressedShift(int b, int x, int y) {
		World map = frame.getMap();
		switch(b) {
		case 1: //LMB
			placeBlock(x, y, map, window);
			break;
		case 2: //MMB
			//Go here
			frame.perspective.x = -x;
			frame.perspective.y = -y;
			break;
		case 3: //RMB
			//Mine
			if(map.removeMachine(x, y)) {
				debug.printl("Removed machine");
				return;
			}
			//Drop if needed
			if(frame.getPlayer().isSurvival()) {
				//The player is survival, requires pickaxe
				return;
			}
			BlockEntry block = frame.getMap().get(x, y);
			block.type().leaveBehind().place(x, y, map);
			break;
		default:
			break;
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		bx = frame.getMouseoverBlockX();
		by = frame.getMouseoverBlockY();
	}
	
	public static void placeBlock(int x, int y, World map, WorldWindow window) {
		//Place the block
		ItemRecord irecord = window.getPlacer().getSelectedValue();
		if(irecord == null) return;
		ItemEntry item = irecord.item();
		if(item instanceof Placer) {
			//If in survival, consume items
			if(window.getPlayer().isSurvival()) {
				//In survival
				int extracted = irecord.extract(1);
				if(extracted == 0) return;
				window.panelPlayerInv.craftGUI.inventoryController.refresh();
				//refresh the inventory
			}
			 ((Placer)item).place(x, y, map);
		}
	}
	
	//Title
	private final String title = $res("toolstd");
	@Override
	public String title() {
		return title;
	}
	
	//Description
	private static final String descr1 = $res("toolstd-1");
	private static final String descr2 = $res("toolstd-2");
	private static final String descr3 = $res("toolstd-3");
	private static final String descr4 = $res("toolstd-4");
	private static final String descr5 = $res("toolstd-5");
	private static final String descr6 = $res("toolstd-6");
	private static final String descr7 = $res("toolstd-7");
	private static final String descr8 = $res("toolstd-8");
	private static final String descr = descr1+'\n'+descr2+'\n'+descr3+'\n'+descr4+'\n'+descr5+'\n'+descr6+'\n'+descr7+'\n'+descr8;
	@Override
	public String description() { return descr; }
	
	//Icon
	@NN public static final Icon ICON_NORMAL = new ImageIcon(Textures.get("tool/normal.png"));
	@Override
	public Icon getIcon() {
		return ICON_NORMAL;
	}
}
