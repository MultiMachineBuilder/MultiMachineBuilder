/**
 * 
 */
package mmb.menu.world.window;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.debug.Debugger;
import mmb.menu.wtool.ToolStandard;
import mmb.world.block.BlockEntry;
import mmb.world.worlds.world.World;

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
		
		if(frame.getPlayer().isCreative()){
			mntmMine = new JMenuItem("Mine (Shift+RMB)");
			mntmMine.addActionListener(e -> {
				if(map.removeMachine(mouseoverX, mouseoverY)) {
					debug.printl("Removed machine");
					return;
				}
				//Drop if needed
				if(frame.getPlayer().isSurvival()) {
					//The player is survival, requires pickaxe
					return;
				}
				block
				.type()
				.leaveBehind()
				.place(mouseoverX, mouseoverY, map);
			});
			add(mntmMine);
		}
		
		
		mntmNewMenuItem_1 = new JMenuItem("Place (Shift+LMB)");
		mntmNewMenuItem_1.addActionListener(e -> {
			ToolStandard.placeBlock(mouseoverX, mouseoverY, map, frame.window);
		});
		add(mntmNewMenuItem_1);
		
		
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
