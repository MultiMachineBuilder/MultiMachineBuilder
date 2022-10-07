/**
 * 
 */
package mmb.menu.world.inv;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;

import com.pploder.events.Event;

import mmb.CatchingEvent;
import mmb.debug.Debugger;
import mmb.world.inventory.ItemRecord;

import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;

/**
 * @author oskar
 * A class to manage pick-and-drop of items
 */
public class InventoryOrchestrator extends JPanel {
	private static final Debugger debug = new Debugger("INVENTORY CONTROLLER");
	public InventoryOrchestrator() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);
		
		lblInventoryWindow = new JLabel("Inventory Window    ");
		add(lblInventoryWindow);
		
		btnCancel = new JButton("Cancel item transfers");
		btnCancel.addActionListener(e -> cancel());
		btnCancel.setBackground(new Color(255, 215, 0));
		add(btnCancel);
		
		lblNumber = new JLabel(" # items to transfer  ");
		add(lblNumber);
		
		spinner = new JSpinner();
		add(spinner);
		
		checkMultiTransfer = new JCheckBox("Mutiple transfers");
		add(checkMultiTransfer);
	}
	private static final long serialVersionUID = 7147216644566680303L;
	private JLabel lblInventoryWindow;
	private JButton btnCancel;
	private JLabel lblNumber;
	private JSpinner spinner;
	private JCheckBox checkMultiTransfer;
	private ItemRecord source;
	
	/**
	 * Cancel any pending item transfers
	 */
	public void cancel() {
		source = null;
	}
	
	/**
	 * @return the source
	 */
	public ItemRecord getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(ItemRecord source) {
		if(this.source == source) return;
		this.source = source;
		itemPicked.trigger(new OrchestratorSelectionEvent(source));
	}

	public class OrchestratorSelectionEvent{
		public final ItemRecord item;
		public final InventoryOrchestrator selector = InventoryOrchestrator.this;
		private OrchestratorSelectionEvent(ItemRecord item) {
			this.item = item;
		}
	}
	public static final Event<OrchestratorSelectionEvent> itemPicked
	= new CatchingEvent<>(debug, "Failed to handle item picked event");
}
