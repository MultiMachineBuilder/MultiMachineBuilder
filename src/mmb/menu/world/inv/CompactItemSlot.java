package mmb.menu.world.inv;

import javax.swing.JPanel;

import mmb.annotations.Nil;
import mmb.data.variables.ListenableValue;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemStack;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class CompactItemSlot extends JPanel {
	private static final long serialVersionUID = 7564222960354813659L;
	
	/** The item stack shown by this item slot*/
	public final ListenableValue<@Nil ItemStack> item = new ListenableValue<>(null);

	public final JLabel itemPicture;
	private final JButton button;
	
	public void setNoQuantity(ItemEntry itm) {
		item.set(new ItemStack(itm, -1));
	}
	
	public CompactItemSlot() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		itemPicture = new JLabel();
		add(itemPicture);
		
		button = new JButton("...");
		add(button);
		
		item.listenadd(x -> updateItem(x));
	}
	public CompactItemSlot(ItemStack stk) {
		this();
		item.set(stk);
	}
	
	private void updateItem(@Nil ItemStack stk) {
		Icon itemIcon = null;
		int count = -1;
		if(stk != null) {
			itemIcon = stk.item.icon();
			count = stk.amount;
		}
		itemPicture.setIcon(itemIcon);
		button.setText((count < 0) ? "..." : Integer.toString(count));
	}
	
	

}
