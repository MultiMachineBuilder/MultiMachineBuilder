/**
 * 
 */
package mmb.WORLD.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.BEANS.Rotable;
import mmb.BEANS.TextMessageProvider;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class WorldMenu extends JPopupMenu {
	private static final long serialVersionUID = -84797957951871754L;
	private JMenuItem mntmGoHere;
	private JMenu mnInventory;
	private JMenuItem mntmWithdrawA;
	private JMenuItem mntmWithdraw1;
	private JMenuItem mntmMine;
	private JMenuItem mntmTextEditor;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmCCW;
	private JMenuItem mntmCW;
	private static final Debugger debug = new Debugger("WORLD MENU");
	private JMenuItem mntmNewMenuItem_1;

	/**
	 * 
	 */
	public WorldMenu(BlockEntry block, WorldFrame frame, WorldWindow window) {
		World map = frame.getMap();
		int mouseoverX = frame.getMouseoverBlockX();
		int mouseoverY = frame.getMouseoverBlockY();
		mntmGoHere = new JMenuItem("Go here");
		mntmGoHere.addActionListener(e ->{
			frame.perspective.x = -mouseoverX;
			frame.perspective.y = -mouseoverY;
		});
		add(mntmGoHere);
		
		mnInventory = new JMenu("Inventory");
		add(mnInventory);
		
		mntmWithdrawA = new JMenuItem("Withdraw all");
		mnInventory.add(mntmWithdrawA);
		
		mntmWithdraw1 = new JMenuItem("Withdraw 1");
		mnInventory.add(mntmWithdraw1);
		
		mntmNewMenuItem = new JMenuItem("Open inventory");
		mnInventory.add(mntmNewMenuItem);
		
		mntmMine = new JMenuItem("Mine");
		mntmMine.addActionListener(e -> {
			if(frame.getMap().removeMachine(mouseoverX, mouseoverY)) {
				debug.printl("Removed machine");
				return;
			}
			block.type().leaveBehind().place(mouseoverX, mouseoverY, map.getMap());
		});
		
		mntmNewMenuItem_1 = new JMenuItem("Place");
		mntmNewMenuItem_1.addActionListener(e -> {
			if(map.inBounds(mouseoverX, mouseoverY)) {
				frame.getPlacer().getPlacer().place(mouseoverX, mouseoverY, map.getMap());
			}
		});
		add(mntmNewMenuItem_1);
		add(mntmMine);
		
		mntmTextEditor = new JMenuItem("Text editor");
		mntmTextEditor.addActionListener(e -> {
			if(!block.isBlockEntity()) return;
			BlockEntity ent = block.asBlockEntity();
			if(ent instanceof TextMessageProvider) {
				TextMessageProvider sv = (TextMessageProvider) ent;
				NewTextEditor textEditor = new NewTextEditor(sv, ent, window);
				window.openDialogWindow(textEditor, textEditor.title);
			}
		});
		add(mntmTextEditor);
		
		mntmCCW = new JMenuItem("Turn CCW (Ctrl+LMB)");
		mntmCCW.addActionListener(e -> {
				if(block instanceof Rotable) 
					((Rotable)block).ccw();
		});
		add(mntmCCW);
		
		mntmCW = new JMenuItem("Turn CW (Ctrl+RMB)");
		mntmCW.addActionListener(e -> {
				if(block instanceof Rotable) 
					((Rotable)block).cw();
		});
		add(mntmCW);
	}
}
