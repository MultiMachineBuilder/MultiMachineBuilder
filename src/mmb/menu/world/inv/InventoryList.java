/**
 * 
 */
package mmb.menu.world.inv;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import mmb.NN;
import mmb.Nil;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import monniasza.collects.ReadOnlyListModel;

/**
 * @author oskar
 *
 */
public class InventoryList extends JList<ItemRecord> {
	private static final long serialVersionUID = -7457316518688988110L;
	
	public final DefaultListModel<ItemRecord> _model;
	public final ListModel<ItemRecord> model;
	private Inventory inv;
	/**
	 * @return the inv
	 */
	public Inventory getInv() {
		return inv;
	}
	/**
	 * @param inv the inv to set
	 */
	public void setInv(Inventory inv) {
		this.inv = inv;
		refresh();
	}
	public InventoryList() {
		this(null);
	}
	public InventoryList(@Nil Inventory inv) {
		this(new DefaultListModel<ItemRecord>(), inv);
		setCellRenderer(new ItemListCellRenderer());		
	}
	private InventoryList(DefaultListModel<ItemRecord> model,@Nil Inventory inv) {
		super(model);
		this._model = model;
		this.model = new ReadOnlyListModel<>(_model);
		this.inv = inv;
	}
	@Override
	public void paint(@SuppressWarnings("null") Graphics g) {
		refresh();
		super.paint(g);
	}
	public void refresh() {
		if(inv == null) return;
		_model.clear();
		for(ItemRecord record: inv) {
			_model.addElement(record);
		}
	}
	
	public static class ItemListCellRenderer extends JLabel implements ListCellRenderer<@NN ItemRecord>{
		private static final long serialVersionUID = -3535344904857285958L;
		public ItemListCellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends ItemRecord> list, ItemRecord itemType, int index,
		boolean isSelected, boolean cellHasFocus) {
			setIcon(itemType.id().icon());
			setText(itemType.id().title() + " * " + itemType.amount());
			
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
