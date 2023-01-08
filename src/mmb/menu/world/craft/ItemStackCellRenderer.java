/**
 * 
 */
package mmb.menu.world.craft;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.engine.inv.ItemStack;

public class ItemStackCellRenderer extends JLabel implements ListCellRenderer<ItemStack>{
	public static final ItemStackCellRenderer instance = new ItemStackCellRenderer();
	private static final long serialVersionUID = -3535344904857285958L;
	private static final Dimension PRESENT = new Dimension(275, 32);
	private static final Dimension ABSENT = new Dimension();
	@Override
	public Component getListCellRendererComponent(
		@SuppressWarnings("null") JList<? extends ItemStack> list,
		@SuppressWarnings("null") ItemStack itemType,
		int index,
		boolean isSelected,
		boolean cellHasFocus
	){
		if(itemType.amount == 0) {
			setPreferredSize(ABSENT);
			setMinimumSize(ABSENT);
		}else {
			setPreferredSize(PRESENT);
			setMinimumSize(PRESENT);
		}
		setOpaque(true);
		setIcon(itemType.item.icon());
		setText(itemType.id().title() + " � " + itemType.amount);
		
		if (isSelected) {
		    setBackground(list.getSelectionBackground());
		    setForeground(list.getSelectionForeground());
		} else {
		    setBackground(list.getBackground());
		    setForeground(list.getForeground());
		}
		return this;
	}
}