/**
 * 
 */
package mmb.WORLD.gui.window;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 *
 */
public class WorldMenu extends JPopupMenu {
	private static final long serialVersionUID = -84797957951871754L;
	private JMenuItem mntmGoHere;
	private JMenuItem mntmMine;
	private JMenuItem mntmCCW;
	private JMenuItem mntmCW;
	private static final Debugger debug = new Debugger("WORLD MENU");
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmOpposite;
	private JMenuItem mntmFlip;
	private JMenuItem mntmMove;

	/**
	 * @param block block clicked with mouse
	 * @param frame world frame clicked with mouse
	 */
	public WorldMenu(BlockEntry block, WorldFrame frame) {
		World map = frame.getMap();
		int mouseoverX = frame.getMouseoverBlockX();
		int mouseoverY = frame.getMouseoverBlockY();
		mntmGoHere = new JMenuItem("Go here (Shift+MMB)");
		mntmGoHere.addActionListener(e ->{
			frame.perspective.x = -mouseoverX;
			frame.perspective.y = -mouseoverY;
		});
		add(mntmGoHere);
		
		mntmMine = new JMenuItem("Mine (Shift+RMB)");
		mntmMine.addActionListener(e -> {
			if(map.removeMachine(mouseoverX, mouseoverY)) {
				debug.printl("Removed machine");
				return;
			}
			//Drop if needed
			if(!frame.getPlayer().creative.getValue()) {
				//The player is survival, requires pickaxe
				return;
			}
			block
			.type()
			.leaveBehind()
			.place(mouseoverX, mouseoverY, map);
		});
		
		mntmNewMenuItem_1 = new JMenuItem("Place (Shift+LMB)");
		mntmNewMenuItem_1.addActionListener(e -> {
			if(map.inBounds(mouseoverX, mouseoverY)) {
				ItemEntry item = frame.getPlacer().getSelectedValue().item();
				if(item instanceof Placer) {
					((Placer)item).place(mouseoverX, mouseoverY, map);
				}
			}
		});
		add(mntmNewMenuItem_1);
		add(mntmMine);
		
		mntmCCW = new JMenuItem("Turn CCW (Ctrl+LMB)");
		mntmCCW.addActionListener(e -> {
			block.wrenchCCW();
		});
		add(mntmCCW);
		
		mntmCW = new JMenuItem("Turn CW (Ctrl+RMB)");
		mntmCW.addActionListener(e -> {
			block.wrenchCW();
		});
		add(mntmCW);
		
		mntmOpposite = new JMenuItem("Rotate around (Ctrl+MMB)");
		add(mntmOpposite);
		
		mntmFlip = new JMenuItem("Flip direction");
		add(mntmFlip);
		
		mntmMove = new JMenuItem("Move the player here");
		mntmMove.addActionListener(e -> {
			map.player.pos.set(mouseoverX + 0.5, mouseoverY + 0.5);
		});
		add(mntmMove);
	}
}
