/**
 * 
 */
package mmb.menu.world.inv;

import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import mmb.NN;
import mmb.Nil;
import mmb.data.variables.Variable;
import mmb.engine.MMBUtils;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.Icons;
import mmb.menu.components.MultilineImageLabel;

import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * Displays an inventory and allows selection of items for 
 * @author oskar
 */
public class InventoryController extends Box implements AbstractInventoryController {
	private static final long serialVersionUID = -3804277344383315579L;
	
	private final JList<ItemRecord> invlist;
	@NN private DefaultListModel<ItemRecord> model0 = new DefaultListModel<>();
	/** Inventory to which this inventory controller is linked */
	private transient Inventory inv;
	private JLabel label;
	private JButton btnRefresh;
	private JScrollPane scrollPane;
	private JButton btnUnsel;
	/** Box at the top of this inventory controller */
	@NN public final Box ubox;

	@Override
	public void refresh() {
		int[] selection = getSelectedIndices();
		if(inv == null) return;
		List<@NN ItemRecord> list = new ArrayList<>(inv);
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
	 * Creates an InventoryController with an inventory
	 * @param inv inventory to use
	 */
	public InventoryController(@Nil Inventory inv) {
		this();
		setInv(inv);
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
	public int @NN [] getSelectedIndices() {
		return invlist.getSelectedIndices();
	}
	/**
	 * @return selected value, or null if not selected
	 * @see javax.swing.JList#getSelectedValue()
	 */
	@Override
	@Nil public ItemRecord getSelectedValue() {
		return invlist.getSelectedValue();
	}
	/** @return item selection variable for item selection slots */
	@NN public Variable<ItemEntry> itemSelection(){
		return Variable.delegate(this::getSelectedItem, MMBUtils.doNothing());
	}
	/** @return a selected item */
	@Nil public ItemEntry getSelectedItem() {
		ItemRecord irecord = getSelectedValue();
		if(irecord == null) return null;
		return irecord.item();
	}
	
	@Override
	@SuppressWarnings("null")
	@NN public List<ItemRecord> getSelectedValuesList() {
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
	public void setInv(@Nil Inventory inv) {
		this.inv = inv;
		configureButtons();
		refresh();
	}

	private void configureButtons() {
		boolean hasInv = inv != null;
		btnRefresh.setEnabled(hasInv);
	}

	private static class CellRenderer extends MultilineImageLabel implements ListCellRenderer<ItemRecord>{
		private static final long serialVersionUID = -3535344904857285958L;
		private final Dimension PRESENT = new Dimension(275, 32);
		private final Dimension ABSENT = new Dimension();
		private static final Debugger debug = new Debugger("INVENTORY CELL RENDERER");
		@Override
		public Component getListCellRendererComponent(
			@SuppressWarnings("null") JList<? extends ItemRecord> list,
			@SuppressWarnings("null") ItemRecord irecord,
			int index,
			boolean isSelected,
			boolean cellHasFocus
		){
			int amount = irecord.amount();
			if(amount == 0) {
				setPreferredSize(ABSENT);
				setMinimumSize(ABSENT);
			}else {
				setPreferredSize(PRESENT);
				setMinimumSize(PRESENT);
			}
			setOpaque(true);
			setIcon(irecord.item().icon());
			String newText = irecord.item().title() + " * " + irecord.amount();
			debug.printl(newText);
			setText(newText);
			
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

	/** @return the display title of this inventory */
	public String getTitle() {
		return label.getText();
	}
	/**
	 * Sets the display title of this inventory
	 * @param title new display title
	 */
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
	@NN public DefaultListModel<ItemRecord> getModel() {
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
	@NN public ListSelectionModel getSelectionModel() {
		return Objects.requireNonNull(invlist.getSelectionModel(), "Invalid selection model");
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
