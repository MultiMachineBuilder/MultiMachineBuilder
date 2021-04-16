/**
 * 
 */
package mmb.WORLD.gui;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import net.miginfocom.swing.MigLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.annotation.Nullable;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * @author oskar
 *
 */
public class InventoryController extends JPanel {
	private static final long serialVersionUID = -3804277344383315579L;
	
	private JList<ItemRecord> invlist;
	private final DefaultListModel<ItemRecord> model = new DefaultListModel<>();
	private Inventory inv;
	private JLabel label;
	private JButton btnDrop;
	private JButton btnPick;
	private JButton btnRefresh;
	private InventoryOrchestrator orchestrator;
	private JScrollPane scrollPane;

	public void refresh() {
		if(inv == null) return;
		model.clear();
		for(ItemRecord record: inv) {
			model.addElement(record);
		}
		repaint();
	}
	/**
	 * Create the panel.
	 */
	public InventoryController() {
		
		setLayout(new MigLayout("ins 0", "[275.00]", "[20.00][grow]"));
		
		label = new JLabel("   Inventory   ");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		add(label, "flowx,cell 0 0,alignx center");
		
		btnDrop = new JButton("Drop here");
		btnDrop.setEnabled(false);
		btnDrop.setBackground(Color.RED);
		add(btnDrop, "cell 0 0,growx");
		
		btnPick = new JButton("Pick");
		btnPick.setEnabled(false);
		btnPick.setBackground(Color.GREEN);
		add(btnPick, "cell 0 0,growx");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1,grow");
		
		invlist = new JList<>();
		scrollPane.setViewportView(invlist);
		invlist.setModel(model);
		invlist.setCellRenderer(new CellRenderer());
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setEnabled(false);
		btnRefresh.setBackground(Color.YELLOW);
		add(btnRefresh, "cell 0 0,growx");
	}

	
	/**
	 * @return
	 * @see javax.swing.JList#getSelectedIndex()
	 */
	public int getSelectedIndex() {
		return invlist.getSelectedIndex();
	}
	/**
	 * @return
	 * @see javax.swing.JList#getSelectedIndices()
	 */
	public int[] getSelectedIndices() {
		return invlist.getSelectedIndices();
	}
	/**
	 * @return
	 * @see javax.swing.JList#getSelectedValue()
	 */
	public ItemRecord getSelectedValue() {
		return invlist.getSelectedValue();
	}

	public List<ItemRecord> getSelectedValuesList() {
		return invlist.getSelectedValuesList();
	}
	/**
	 * @param arg0
	 * @see javax.swing.JList#setSelectedIndex(int)
	 */
	public void setSelectedIndex(int arg0) {
		invlist.setSelectedIndex(arg0);
	}
	/**
	 * @param arg0
	 * @see javax.swing.JList#setSelectedIndices(int[])
	 */
	public void setSelectedIndices(int[] arg0) {
		invlist.setSelectedIndices(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @see javax.swing.JList#setSelectedValue(java.lang.Object, boolean)
	 */
	public void setSelectedValue(Object arg0, boolean arg1) {
		invlist.setSelectedValue(arg0, arg1);
	}
	
	/**
	 * @return the inv
	 */
	public Inventory getInv() {
		return inv;
	}
	/**
	 * Sets the inventory of this list
	 * @param inv2 the new inventory
	 */
	public void setInv(@Nullable Inventory inv) {
		this.inv = inv;
		configureButtons();
		refresh();
	}
		
	/**
	 * @return the current inventory orchestrator
	 */
	public @Nullable InventoryOrchestrator getOrchestrator() {
		return orchestrator;
	}
	/**
	 * Set the new inventory orchestrator
	 * @param orchestrator new inventory orchestrator
	 */
	public void setOrchestrator(@Nullable InventoryOrchestrator orchestrator) {
		this.orchestrator = orchestrator;
		configureButtons();
	}

	private void configureButtons() {
		boolean hasOrch = orchestrator != null;
		boolean hasInv = inv != null;
		boolean both = hasInv && hasOrch;
		btnRefresh.setEnabled(hasInv);
		btnPick.setEnabled(both);
		btnDrop.setEnabled(both);
	}

	private static class CellRenderer extends JLabel implements ListCellRenderer<ItemRecord>{
		private static final long serialVersionUID = -3535344904857285958L;
		private final Dimension PRESENT = new Dimension(275, 32);
		private final Dimension ABSENT = new Dimension();
		@Override
		public Component getListCellRendererComponent(
			@SuppressWarnings("null") JList<? extends ItemRecord> list,
			@SuppressWarnings("null") ItemRecord itemType,
			int index,
			boolean isSelected,
			boolean cellHasFocus
		){
			int amount = itemType.amount();
			if(amount == 0) {
				setPreferredSize(ABSENT);
				setMinimumSize(ABSENT);
			}else {
				setPreferredSize(PRESENT);
				setMinimumSize(PRESENT);
			}
			setOpaque(true);
			setIcon(itemType.id().type().getIcon());
			setText(itemType.id().type().title() + " × " + itemType.amount());
			
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

	public String getTitle() {
		return label.getText();
	}
	public void setTitle(String title) {
		label.setText(title);
	}
}
