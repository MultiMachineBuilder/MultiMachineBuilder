/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.StringValue;
import mmb.debug.Debugger;

import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuBar;

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
	private final MouseEvent mouse;
	private final WorldFrame f;
	private JMenuItem mntmMine;
	private JMenuItem mntmTextEditor;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
	private static final Debugger debug = new Debugger("WORLD MENU");

	/**
	 * 
	 */
	public WorldMenu(BlockEntry entry, MouseEvent event, WorldFrame frame, WorldWindow window) {
		mouse = event;
		block = entry;
		f = frame;
		mntmGoHere = new JMenuItem("Go here");
		mntmGoHere.addActionListener(e ->{
			f.perspective.x = -block.x;
			f.perspective.y = -block.y;
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
			if(block.owner.removeMachine(block.x, block.y)) {
				debug.printl("Removed machine");
				return;
			}
			block.owner.place(block.x, block.y, block.type.leaveBehind);
		});
		add(mntmMine);
		
		mntmTextEditor = new JMenuItem("Text editor");
		mntmTextEditor.addActionListener(e -> {
				BlockProperty bpValue = block.getProperty("value");
				if(bpValue instanceof StringValue) {
					StringValue sv = (StringValue) bpValue;
					/*new TextEditor(sv, block).setVisible(true);*/
					NewTextEditor textEditor = new NewTextEditor(sv, block, window);
					window.openDialogWindow(textEditor, textEditor.title);
				}
		});
		add(mntmTextEditor);
		
		mntmNewMenuItem_1 = new JMenuItem("Turn CCW (Ctrl+LMB)");
		add(mntmNewMenuItem_1);
		
		mntmNewMenuItem_2 = new JMenuItem("Turn CW (Ctrl+RMB)");
		add(mntmNewMenuItem_2);
	}
}
