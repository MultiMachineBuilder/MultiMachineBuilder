/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.WORLD.block.SkeletalBlockEntity;
import mmb.BEANS.TextMessageProvider;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.blocks.StringValue;
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
	private final transient BlockEntry block;
	private final WorldFrame f;
	private JMenuItem mntmMine;
	private JMenuItem mntmTextEditor;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmCCW;
	private JMenuItem mntmCW;
	private static final Debugger debug = new Debugger("WORLD MENU");

	/**
	 * 
	 */
	public WorldMenu(BlockEntry blockEntry, MouseEvent event, WorldFrame frame, WorldWindow window) {
		block = blockEntry;
		f = frame;
		int mouseoverX = frame.getMouseoverBlockX();
		int mouseoverY = frame.getMouseoverBlockY();
		mntmGoHere = new JMenuItem("Go here");
		mntmGoHere.addActionListener(e ->{
			f.perspective.x = -mouseoverX;
			f.perspective.y = -mouseoverY;
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
			block.type().leaveBehind().place(mouseoverX, mouseoverY, null);
		});
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
		add(mntmCCW);
		
		mntmCW = new JMenuItem("Turn CW (Ctrl+RMB)");
		add(mntmCW);
	}
}
