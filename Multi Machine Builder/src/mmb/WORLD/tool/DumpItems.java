/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class DumpItems extends WindowTool {

	public DumpItems() {
		super("dumpItems");
	}

	@Override
	public String title() {
		return "Dump items";
	}

	public static final Icon icon = new ImageIcon(Textures.get("dropItems.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}

	@Override
	public void preview(int startX, int startY, int scale, Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point trashed = frame.blockAt(e.getPoint());
		Placer placer = window.getPlacer().getSelectedValue();
		switch(e.getButton()) {
		case 1: //LMB drop
			if(placer instanceof ItemEntry) {
				ItemEntry item = (ItemEntry) placer;
				Toolkit.getDefaultToolkit().beep();
				frame.getMap().dropItem(item, trashed.x, trashed.y);
			}
			break;
		case 3: //RMB pick up
			Collection<ItemEntry> items = frame.getMap().getDrops(trashed.x, trashed.y);
			Iterator<ItemEntry> iter = items.iterator();
			Toolkit.getDefaultToolkit().beep();
			while(iter.hasNext()) {
				ItemEntry item = iter.next();
				int tf = window.getPlayer().inv.insert(item, 1);
				if(tf == 1) iter.remove();
			}
		}
		
		
	}

}
