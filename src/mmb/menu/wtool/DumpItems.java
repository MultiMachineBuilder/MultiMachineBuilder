/**
 * 
 */
package mmb.menu.wtool;

import static mmb.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.texture.Textures;
import mmb.world.inventory.ItemRecord;
import mmb.world.item.ItemEntry;
import mmb.world.items.ContentsItems;

/**
 * @author oskar
 *
 */
public class DumpItems extends WindowTool {

	public DumpItems() {
		super("dumpItems");
	}

	private final String title = $res("dump");
	@Override
	public String title() {
		return title;
	}

	public static final Icon icon = new ImageIcon(Textures.get("dropItems.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		// unused
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point trashed = frame.blockAt(e.getPoint());
		switch(e.getButton()) {
		case 1: //LMB drop
			ItemRecord irecord = window.getPlacer().getSelectedValue();
			if(irecord == null) return;
			ItemEntry item = irecord.item();
			//In survival, the Item Bucket cannot be dropped
			if(window.getPlayer().isSurvival() && item == ContentsItems.bucket) return;
			int extract = window.getPlayer().inv.extract(item, 1);
			if(extract == 1)
				frame.getMap().dropItem(item, trashed.x, trashed.y);
			break;
		case 3: //RMB pick up
			Collection<ItemEntry> items = frame.getMap().getDrops(trashed.x, trashed.y);
			Iterator<ItemEntry> iter = items.iterator();
			while(iter.hasNext()) {
				ItemEntry item0 = iter.next();
				if(item0 == null) {
					iter.remove();
					continue;
				}
				int tf = window.getPlayer().inv.insert(item0, 1);
				if(tf == 1) iter.remove();
			}
		}
		
		
	}

}
