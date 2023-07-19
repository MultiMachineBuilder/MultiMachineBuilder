/**
 * 
 */
package mmb.menu.world.window;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mmb.content.ContentsItems;
import mmb.engine.block.BlockEntry;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.world.World;
import mmb.menu.wtool.ToolStandard;

/**
 * A pop-up meny when clicking right with a standard tool
 * @author oskar
 */
public class WorldPopup extends JPopupMenu {
	private static final long serialVersionUID = -84797957951871754L;
	private JMenuItem mntmGoHere;
	private JMenuItem mntmMine;
	private JMenuItem mntmCCW;
	private JMenuItem mntmCW;
	private JMenuItem mntmDropASingItem;
	private JMenuItem mntmDropASingItemStack;
	private JMenuItem mntmPickUpSingItem;
	private JMenuItem mntmPickUpAllItem;

	private static final Debugger debug = new Debugger("WORLD MENU");
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmOpposite;
	private JMenuItem mntmFlip;
	private JMenuItem mntmMove;

	/**
	 * @param block block clicked with mouse
	 * @param frame world frame clicked with mouse
	 */
	public WorldPopup(BlockEntry block, WorldFrame frame) {
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
		mntmNewMenuItem_1.addActionListener(e ->  ToolStandard.placeBlock(mouseoverX, mouseoverY, map, frame.window));
		add(mntmNewMenuItem_1);

		mntmDropASingItem = new JMenuItem("DropASingItem (Alt+LMB)");
		mntmDropASingItem.addActionListener(e -> dropItems(frame.window, mouseoverX, mouseoverY, 1));
		add(mntmDropASingItem);

		mntmDropASingItemStack = new JMenuItem("DropASingItemStack (Alt+Ctrl+LMB)");
		mntmDropASingItemStack.addActionListener(e -> dropItems(frame.window, mouseoverX, mouseoverY, Integer.MAX_VALUE));
		add(mntmDropASingItemStack);

		mntmPickUpSingItem = new JMenuItem("PickUpSingItem (Alt+RMB)");
		mntmPickUpSingItem.addActionListener(e -> pickupItems(frame.window, mouseoverX, mouseoverY, true));
		add(mntmPickUpSingItem);

		mntmPickUpAllItem = new JMenuItem("PickUpAllItem (Alt+Ctrl+RMB)");
		mntmPickUpAllItem.addActionListener(e -> pickupItems(frame.window, mouseoverX, mouseoverY, false));
		add(mntmPickUpAllItem);
		
		
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
	
	/**
	 * Picks up items in a world window
	 * @param ww world window to use
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 * @param one should only one item be picked up?
	 */
	public static void pickupItems(WorldWindow ww, int x, int y, boolean one) {
		Collection<ItemEntry> items = ww.getMap().getDrops(x, y);
		Iterator<ItemEntry> iter = items.iterator();
		while(iter.hasNext()) {
			ItemEntry item0 = iter.next();
			if(item0 == null) {
				iter.remove();
				continue;
			}
			int tf = ww.getPlayer().inv.insert(item0, 1);
			ww.panelPlayerInv.craftGUI.inventoryController.refresh();
			if(tf == 1) iter.remove();
			if(one) return;
		}
	}
	/**
	 * Drops items in a world window
	 * @param ww world window to use
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 * @param max maximum items to drop
	 */
	public static void dropItems(WorldWindow ww, int x, int y, int max) {
		ItemRecord irecord = ww.getPlacer().getSelectedValue();
		if(irecord == null) return;
		ItemEntry item = irecord.item();
		int extract = ww.getPlayer().inv.extract(item, max);
		ww.getMap().dropItem(item, extract, x, y);
		ww.panelPlayerInv.craftGUI.inventoryController.refresh();
	}
}
