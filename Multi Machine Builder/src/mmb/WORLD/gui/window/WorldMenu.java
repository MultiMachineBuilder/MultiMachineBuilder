/**
 * 
 */
package mmb.WORLD.gui.window;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.BEANS.Rotable;
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
	private JMenuItem mntmMine;
	private JMenuItem mntmCCW;
	private JMenuItem mntmCW;
	private static final Debugger debug = new Debugger("WORLD MENU");
	private JMenuItem mntmNewMenuItem_1;

	/**
	 * 
	 */
	public WorldMenu(BlockEntry block, WorldFrame frame) {
		World map = frame.getMap();
		int mouseoverX = frame.getMouseoverBlockX();
		int mouseoverY = frame.getMouseoverBlockY();
		mntmGoHere = new JMenuItem("Go here");
		mntmGoHere.addActionListener(e ->{
			frame.perspective.x = -mouseoverX;
			frame.perspective.y = -mouseoverY;
		});
		add(mntmGoHere);
		
		mntmMine = new JMenuItem("Mine");
		mntmMine.addActionListener(e -> {
			if(map.removeMachine(mouseoverX, mouseoverY)) {
				debug.printl("Removed machine");
				return;
			}
			//Drop if needed
			if(!frame.getPlayer().creative.getValue()) {
				//The player is survival, drop
			}
			block
			.type()
			.leaveBehind()
			.place(mouseoverX, mouseoverY, map.getMap());
		});
		
		mntmNewMenuItem_1 = new JMenuItem("Place");
		mntmNewMenuItem_1.addActionListener(e -> {
			if(map.inBounds(mouseoverX, mouseoverY)) {
				frame.getPlacer().getPlacer().place(mouseoverX, mouseoverY, map.getMap());
			}
		});
		add(mntmNewMenuItem_1);
		add(mntmMine);
		
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
