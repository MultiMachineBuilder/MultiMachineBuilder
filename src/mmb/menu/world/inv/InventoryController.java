/**
 * 
 */
package mmb.menu.world.inv;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import mmb.GlobalSettings;
import mmb.Lambdas;
import mmb.data.variables.Variable;
import mmb.menu.Icons;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.items.ItemEntry;
import net.miginfocom.swing.MigLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * @author oskar
 *
 */
public class InventoryController extends Box implements AbstractInventoryController {
	private static final long serialVersionUID = -3804277344383315579L;
	
	private final JList<ItemRecord> invlist;
	@Nonnull private DefaultListModel<ItemRecord> model0 = new DefaultListModel<>();
	
	private Inventory inv;
	private JLabel label;
	private JButton btnRefresh;
	private InventoryOrchestrator orchestrator;
	private JScrollPane scrollPane;
	private JButton btnUnsel;
	@Nonnull public final Box ubox;

	@Override
	public void refresh() {
		int[] selection = getSelectedIndices();
		if(inv == null) return;
		List<ItemRecord> list = new ArrayList<>(inv);
		if(GlobalSettings.sortItems.getValue()) {
			//Items should be sorted
			list.sort((a, b) -> a.item().title().compareTo(b.item().title()));
		}
		model0.clear();
		for(ItemRecord irecord: list) {
			model0.addElement(irecord);
		}
		repaint();
		setSelectedIndices(selection);
	}
	/**
	 * Create the panel.
	 */
	public InventoryController() {
		super(BoxLayout.Y_AXIS);
		
		ubox = Box.createHorizontalBox();
		add(ubox, "cell 0 0,growx");
		
		scrollPane = new JScrollPane();
		add(scrollPane);
		
		invlist = new JList<>();
		scrollPane.setViewportView(invlist);
		invlist.setModel(model0);
		invlist.setCellRenderer(new CellRenderer());
		
		//Upper box
		label = new JLabel($res("wgui-inv"));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		ubox.add(label);
		
		btnRefresh = new JButton(Icons.refresh);
		btnRefresh.addActionListener(e -> refresh());
		btnRefresh.setEnabled(false);
		btnRefresh.setBackground(Color.YELLOW);
		ubox.add(btnRefresh);
		
		btnUnsel = new JButton(Icons.unsel);
		btnUnsel.addActionListener(e -> {
			invlist.setSelectedValue(null, true);
			invlist.setSelectedIndex(-1);
		});
		btnUnsel.setBackground(Color.BLUE);
		ubox.add(btnUnsel);
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
		this();
		set(other);
	}
	/**
	 * @return selected index, or -1 if not selected
	 * @see javax.swing.JList#getSelectedIndex()
	 */
	public int getSelectedIndex() {
		return invlist.getSelectedIndex();
	}
	/**
	 * @return selected indices list
	 * @see javax.swing.JList#getSelectedIndices()
	 */
	@SuppressWarnings("null")
	@Nonnull public int[] getSelectedIndices() {
		return invlist.getSelectedIndices();
	}
	/**
	 * @return selected value, or null if not selected
	 * @see javax.swing.JList#getSelectedValue()
	 */
	@Override
	public ItemRecord getSelectedValue() {
		return invlist.getSelectedValue();
	}
	/** @return item selection variable for item selection slots */
	@Nonnull public Variable<ItemEntry> itemSelection(){
		return Variable.delegate(() -> getSelectedValue().item(), Lambdas.doNothing());
	}
	
	@Override
	@SuppressWarnings("null")
	@Nonnull public List<ItemRecord> getSelectedValuesList() {
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
	@Override
	public Inventory getInv() {
		return inv;
	}
	/**
	 * Sets the inventory of this list
	 * @param inv the new inventory
	 */
	public void setInv(@Nullable Inventory inv) {
		this.inv = inv;
		configureButtons();
		refresh();
	}
	@Override
	public InvType getInvType() {
		return InvType.SIMPLE;
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
		boolean hasInv = inv != null;
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
			setIcon(itemType.item().icon());
			setText(itemType.id().title() + " � " + itemType.amount());
			
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
	 * Replaces the model, selection and inventory with those of the other inventory controller
	 * @param other source of configuration
	 */
	public void set(InventoryController other) {
		setInv(other.getInv());
		setSelectionModel(other.getSelectionModel());
		setSelectionMode(other.getSelectionMode());
		setModel(other.getModel());
	}
	
	/**
	 * @return the model
	 */
	@Nonnull public DefaultListModel<ItemRecord> getModel() {
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
	 * @return current selection mode
	 * @see javax.swing.JList#getSelectionMode()
	 */
	public int getSelectionMode() {
		return invlist.getSelectionMode();
	}
	/**
	 * @return current selection model
	 * @see javax.swing.JList#getSelectionModel()
	 */
	@Nonnull public ListSelectionModel getSelectionModel() {
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
