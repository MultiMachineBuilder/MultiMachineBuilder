/**
 * 
 */
package mmb.WORLD.gui.inv;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import mmb.WORLD.inventory.Inventories;
import mmb.WORLD.inventory.ItemRecord;

/**
 * @author oskar
 *
 */
public class MoveItems extends JPanel {
	private static final long serialVersionUID = -2930606565407970513L;

	/**
	 * Indicates that no buttons will be constructed
	 */
	public static final byte NONE = 0;
	/**
	 * Indicates that buttons will point only to the left
	 */
	public static final byte LEFT = 2;
	/**
	 * Indicates that button will point only to the right
	 */
	public static final byte RIGHT = 1;
	/**
	 * Indicates that button will point to both sides
	 */
	public static final byte BOTH = 3;
	
	/**
	 * Creates a item movement panel with allowed movement directions allowed
	 * @param left the left inventory controller
	 * @param right the right inventory controller
	 * @throws NullPointerException when any inventory controller is {@code null}
	 * @wbp.parser.constructor
	 */
	public MoveItems(InventoryController left, InventoryController right) {
		this(left, right, BOTH);
	}
	public final GridBagLayout layout;
	public final GridBagConstraints constraints;
	/**
	 * Creates a item movement panel with specificallowed movement directions
	 * @param left the left inventory controller
	 * @param right the right inventory controller
	 * @param sides byte tag of sides. Use {@link #NONE}, {@link #LEFT}, {@link #RIGHT} or {@link #BOTH}
	 * @throws NullPointerException when any inventory controller is {@code null}
	 */
	public MoveItems(InventoryController left, InventoryController right, byte sides) {
		Objects.requireNonNull(left, "left is null");
		Objects.requireNonNull(right, "right is null");
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weighty = 1;
		constraints.weightx = 1;
		setLayout(layout);
		if((sides & LEFT) != 0) {
			JButton btnAllL = new JButton("<< All");
			btnAllL.addActionListener(e -> {
				Inventories.transfer(right.getInv(), left.getInv());
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnAllL, constraints);
			add(btnAllL);
			
			JButton btnSelL = new JButton("<< Selection");
			btnSelL.addActionListener(e -> {
				for(ItemRecord selRecord: right.getSelectedValuesList()) {
					Inventories.transfer(selRecord, left.getInv());
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnSelL, constraints);
			add(btnSelL);
			
			JButton btnOPSelL = new JButton("<< Once per selection");
			btnOPSelL.addActionListener(e -> {
				for(ItemRecord selRecord: right.getSelectedValuesList()) {
					Inventories.transfer(selRecord, left.getInv(), 1);
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOPSelL, constraints);
			add(btnOPSelL);
			
			JButton btnOneL = new JButton("<< One");
			btnOneL.addActionListener(e -> {
				Inventories.transfer(right.getSelectedValue(), left.getInv(), 1);
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOneL, constraints);
			add(btnOneL);
		}
		if((sides & RIGHT) != 0) {
			JButton btnOneR = new JButton("One >>");
			btnOneR.addActionListener(e -> {
				Inventories.transfer(left.getSelectedValue(), right.getInv(), 1);
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOneR, constraints);
			add(btnOneR);
			
			JButton btnOPSelR = new JButton("Once per selection >>");
			btnOPSelR.addActionListener(e -> {
				for(ItemRecord selRecord: left.getSelectedValuesList()) {
					Inventories.transfer(selRecord, right.getInv(), 1);
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOPSelR, constraints);
			add(btnOPSelR);
			
			JButton btnSelR = new JButton("Selection >>");
			btnSelR.addActionListener(e -> {
				for(ItemRecord selRecord: left.getSelectedValuesList()) {
					Inventories.transfer(selRecord, right.getInv());
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnSelR, constraints);
			add(btnSelR);
					
			JButton btnAllR = new JButton("All >>");
			btnAllR.addActionListener(e -> {
				Inventories.transfer(left.getInv(), right.getInv());
				left.refresh();
				right.refresh();
			});layout.setConstraints(btnAllR, constraints);
			add(btnAllR);
		}
	}
	public void addAdditionalComponent(Component comp) {
		layout.setConstraints(comp, constraints);
		add(comp);
	}
}
