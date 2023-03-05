/**
 * 
 */
package mmb.menu.wtool;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.content.ContentsItems;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.texture.Textures;

/**
 * Item bucket tool
 * @author oskar
 * @see mmb.content.ContentsItems#bucket
 */
public class DumpItems extends WindowTool {
	/** Creates a dump items tool */
	public DumpItems() {
		super("dumpItems");
	}
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		// unused
	}

	//Event listeners
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
			break;
		default:
			break;
		}
		
		
	}

	//Title
	private final String title = $res("dump");
	@Override
	public String title() {
		return title;
	}
	
	//Icon
	public static final Icon icon = new ImageIcon(Textures.get("dropItems.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}
}
