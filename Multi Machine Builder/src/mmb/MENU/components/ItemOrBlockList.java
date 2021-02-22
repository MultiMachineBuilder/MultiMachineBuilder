/**
 * 
 */
package mmb.MENU.components;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.WORLD.item.Item;

/**
 * @author oskar
 *
 */
public class ItemOrBlockList extends JList<Item> {
	public ItemOrBlockList() {
		super();
		setCellRenderer(new CellRenderer());
	}
	protected class CellRenderer implements ListCellRenderer<Item>{
		@Override
		public Component getListCellRendererComponent(JList<? extends Item> arg0, Item arg1, int arg2,
				boolean arg3, boolean arg4) {
			return new ItemLabel(arg1);
		}
		
	}
}
