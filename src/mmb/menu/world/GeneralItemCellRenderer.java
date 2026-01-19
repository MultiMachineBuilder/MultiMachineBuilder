package mmb.menu.world;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.annotations.Nil;

/**
 * An item cell renderer with an icon and title. Can also be used as an item display.
 * @param <T> type of items/rows/records
 */
public abstract class GeneralItemCellRenderer<T> extends Box implements ListCellRenderer<T> {
	private static final long serialVersionUID = -8150620339101289876L;
	private final JLabel icon;
	private final MultilineLabel label;
	
	/** Abstract constructor for superclasses */
	public GeneralItemCellRenderer() {
		super(BoxLayout.X_AXIS);
		
		icon = new JLabel();
		add(icon);
		icon.setSize(ICON_SIZE);
		
		label = new MultilineLabel();
		add(label);
	}
	
	/**
	 * Gets the icon from an item
	 * @param item row to get icon from
	 * @return the item's icon or null
	 */
	@Nil public abstract Icon getIcon(T item);
	/**
	 * Gets the description of an item. Max 2 rows.
	 * @param item row to get text from
	 * @return the item' title
	 */
	public abstract String getText(T item);
	/**
	 * Checks if the stack is empty.
	 * @param item row to test
	 * @return is the given row empty?
	 */
	public abstract boolean isStackEmpty(T item);
	
	private static final Dimension PRESENT = new Dimension(275, 32);
	private static final Dimension ABSENT = new Dimension();
	private static final Dimension ICON_SIZE = new Dimension(32, 32);
	
	public void setUp(T row) {
		var item = getIcon(row);
		var tooltip = getText(row);
		
		if(isStackEmpty(row)) {
			setPreferredSize(ABSENT);
			setMinimumSize(ABSENT);
		}else {
			setPreferredSize(PRESENT);
			setMinimumSize(PRESENT);
		}
		setOpaque(true);
		icon.setIcon(item);
		label.setText(tooltip);
	}
	
	@Override
	public Component getListCellRendererComponent(
		@SuppressWarnings("null") JList<? extends T> list,
		T row,
		int index,
		boolean isSelected,
		boolean cellHasFocus
	){
		setUp(row);
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
