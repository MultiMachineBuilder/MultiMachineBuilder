/**
 * 
 */
package mmb.MENU;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class ItemOrBlockList extends JList<ItemType> {
	public ItemOrBlockList() {
		super();
		setCellRenderer(new CellRenderer());
	}
	protected class CellRenderer implements ListCellRenderer<ItemType>{
		@Override
		public Component getListCellRendererComponent(JList<? extends ItemType> arg0, ItemType arg1, int arg2,
				boolean arg3, boolean arg4) {
			return new ItemLabel(arg1);
		}
		
	}
}
