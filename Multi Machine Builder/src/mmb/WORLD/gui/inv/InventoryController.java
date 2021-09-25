/**
 * 
 */
package mmb.WORLD.gui.inv;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

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
import javax.swing.event.ListSelectionListener;

/**
 * @author oskar
 *
 */
public class InventoryController extends JPanel {
	private static final long serialVersionUID = -3804277344383315579L;
	
	private final JList<ItemRecord> invlist;
	private DefaultListModel<ItemRecord> model0 = new DefaultListModel<>();
	
	
	private Inventory inv;
	private JLabel label;
	private JButton btnRefresh;
	private InventoryOrchestrator orchestrator;
	private JScrollPane scrollPane;
	private JButton btnUnsel;

	public void refresh() {
		int[] selection = getSelectedIndices();
		if(inv == null) return;
		model0.clear();
		for(ItemRecord record: inv) {
			model0.addElement(record);
		}
		repaint();
		setSelectedIndices(selection);
	}
	/**
	 * Create the panel.
	 */
	public InventoryController() {
		
		setLayout(new MigLayout("ins 0", "[grow]", "[20.00][grow]"));
		
		label = new JLabel("   Inventory   ");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		add(label, "flowx,cell 0 0,alignx center");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1,grow");
		
		invlist = new JList<>();
		scrollPane.setViewportView(invlist);
		invlist.setModel(model0);
		invlist.setCellRenderer(new CellRenderer());
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(e -> refresh());
		btnRefresh.setEnabled(false);
		btnRefresh.setBackground(Color.YELLOW);
		add(btnRefresh, "cell 0 0,growx");
		
		btnUnsel = new JButton("Unselect");
		btnUnsel.addActionListener(e -> {
			invlist.setSelectedValue(null, true);
			invlist.setSelectedIndex(-1);
		});
		btnUnsel.setBackground(Color.BLUE);
		add(btnUnsel, "cell 0 0,growx");
	}
	/**
	 * @param out
	 */
	public InventoryController(@Nullable Inventory out) {
		this();
		setInv(out);
	}
	/**
	 * Creates an InventoryController which mirrors other InventoryController, while also being a different component
	 * @param other
	 */
	public InventoryController(InventoryController other) {
		this(other.inv);
		setSelectionModel(other.getSelectionModel());
		setSelectionMode(other.getSelectionMode());
		setModel(other.getModel());
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
			setText(itemType.id().title() + " × " + itemType.amount());
			
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
	
	
	/**
	 * @return the model
	 */
	public DefaultListModel<ItemRecord> getModel() {
		return model0;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(DefaultListModel<ItemRecord> model) {
		this.model0 = model;
		invlist.setModel(model);
	}
	/**
	 * @return
	 * @see javax.swing.JList#getSelectionMode()
	 */
	public int getSelectionMode() {
		return invlist.getSelectionMode();
	}
	/**
	 * @return
	 * @see javax.swing.JList#getSelectionModel()
	 */
	public ListSelectionModel getSelectionModel() {
		return invlist.getSelectionModel();
	}
	/**
	 * @param selectionMode
	 * @see javax.swing.JList#setSelectionMode(int)
	 */
	public void setSelectionMode(int selectionMode) {
		invlist.setSelectionMode(selectionMode);
	}
	/**
	 * @param selectionModel
	 * @see javax.swing.JList#setSelectionModel(javax.swing.ListSelectionModel)
	 */
	public void setSelectionModel(ListSelectionModel selectionModel) {
		invlist.setSelectionModel(selectionModel);
	}
	/**
	 * @param listener
	 * @see javax.swing.JList#addListSelectionListener(javax.swing.event.ListSelectionListener)
	 */
	public void addListSelectionListener(ListSelectionListener listener) {
		invlist.addListSelectionListener(listener);
	}
	/**
	 * @param listener
	 * @see javax.swing.JList#removeListSelectionListener(javax.swing.event.ListSelectionListener)
	 */
	public void removeListSelectionListener(ListSelectionListener listener) {
		invlist.removeListSelectionListener(listener);
	}	
}
