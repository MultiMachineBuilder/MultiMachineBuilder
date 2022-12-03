/**
 * 
 */
package mmb.menu.world.inv;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.item.ItemEntry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author oskar
 *
 */
public class SingleInventoryController extends Box implements AbstractInventoryController {
	
	private final SingleItemInventory inv;
	private final JLabel label;
	private final JLabel title;
	public SingleInventoryController(SingleItemInventory inv) {
		super(BoxLayout.Y_AXIS);
		this.inv = inv;
		
		title = new JLabel($res("wgui-inv"));
		title.setHorizontalAlignment(SwingConstants.LEFT);
		add(title);
		
		JButton button = new JButton($res("wgui-refresh"));
		button.addActionListener(e -> refresh());
		button.setBackground(Color.YELLOW);
		add(button);
		
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.LEFT);
		add(label);
		
		refresh();
	}

	@Override
	public Inventory getInv() {
		return inv;
	}

	@Override
	public InvType getInvType() {
		return InvType.SINGLE;
	}

	@Override
	public void refresh() {
		ItemEntry item = inv.getContents();
		if(item == null) {
			label.setText("(none)");
			label.setIcon(null);
			label.setToolTipText(null);
		}else {
			label.setText(item.title());
			label.setToolTipText(item.description());
			label.setIcon(item.icon());
		}
	}

	@Override
	public List<ItemRecord> getSelectedValuesList() {
		ItemRecord irecord = getSelectedValue();
		if(irecord == null) return Collections.emptyList();
		return List.of(irecord);
	}

	@Override
	public ItemRecord getSelectedValue() {
		return inv.nget();
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	public String getTitle() {
		return title.getText();
	}

}
