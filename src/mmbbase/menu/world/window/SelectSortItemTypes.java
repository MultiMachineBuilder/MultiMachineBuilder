/**
 * 
 */
package mmb.menu.world.window;

import java.awt.Component;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.menu.world.CreativeItemList;
import mmb.menu.world.window.TabInventory.Tagsel;

/**
 * @author oskar
 *
 */
public class SelectSortItemTypes extends JList<SortItemTypes> {
	private static final long serialVersionUID = -5273806463131131444L;
	
	public SelectSortItemTypes(Supplier<JList<Tagsel>> tgt) {
		Vector<SortItemTypes> model = new Vector<>();
		model.addAll(Arrays.asList(SortItemTypes.values()));
		setListData(model);
		setSelectedIndex(1);
		addListSelectionListener(e -> {
			SortItemTypes sort = getSelectedValue();
			if(sort != null) CreativeItemList.resort(sort, tgt.get().getSelectedValue().eligible());
		});
		setCellRenderer(new CellRenderer());
	}
	private class CellRenderer extends JLabel implements ListCellRenderer<SortItemTypes>{
		private static final long serialVersionUID = 6982597426280368648L;
		public CellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends SortItemTypes> list, @SuppressWarnings("null") SortItemTypes sort, int index,
		boolean isSelected, boolean cellHasFocus) {
			setText(sort.title);
			
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
}
