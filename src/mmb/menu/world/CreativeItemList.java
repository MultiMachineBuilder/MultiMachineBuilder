/**
 * 
 */
package mmb.menu.world;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.NN;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;
import monniasza.collects.Collects;

/**
 * @author oskar
 *
 */
public class CreativeItemList extends JList<ItemType> {
	private static final long serialVersionUID = -5883411113161667818L;
	public CreativeItemList() {
		setModel(model);
		setToolTipText("");
	}	@NN public static final DefaultListModel<ItemType> model = new DefaultListModel<>();
	@NN public static final List<ItemType> list = Collects.toWritableList(model);
	static {
		list.addAll(Items.items);
		Collections.sort(list, (l, r) -> l.title().compareTo(r.title()));
	}
	public static void resort(Comparator<ItemType> sort, DefaultListModel<ItemType> model) {
		Collections.sort(Collects.toWritableList(model), sort);
	}
	@Override
	public ListCellRenderer<? super ItemType> getCellRenderer() {
		return cellrender;
	}

	private final ListCellRenderer<ItemType> cellrender = new ItemListCellRenderer();
	public static class ItemListCellRenderer extends JLabel implements ListCellRenderer<@NN ItemType>{
		private static final long serialVersionUID = -3535344904857285958L;
		public ItemListCellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends ItemType> list, ItemType itemType, int index,
		boolean isSelected, boolean cellHasFocus) {
			setIcon(itemType.getIcon());
			setText(itemType.title());
			
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
	@Override
	public String getToolTipText(@SuppressWarnings("null") MouseEvent event) {
		int index = locationToIndex(event.getPoint());
		if(index > -1) {
			String description = model.get(index).description();
			return description;
		}
		return null;
	}
}
