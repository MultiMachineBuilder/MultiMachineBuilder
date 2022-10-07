/**
 * 
 */
package mmb.menu.world.inv;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import mmb.world.inventory.Inventories;
import mmb.world.inventory.ItemRecord;

import static mmb.GlobalSettings.*;

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
	
	private static final String ALL = $res("wguim-all");
	private static final String SEL = $res("wguim-sel");
	private static final String OSEL = $res("wguim-opsel");
	private static final String ONE = $res("wguim-one");
	private static final String NSEL = $res("wguim-nsel");
	private static final String N = $res("wguim-n");
	
	/**
	 * Creates a item movement panel with allowed movement directions allowed
	 * @param left the left inventory controller
	 * @param right the right inventory controller
	 * @throws NullPointerException when any inventory controller is {@code null}
	 * @wbp.parser.constructor
	 */
	public MoveItems(AbstractInventoryController left, AbstractInventoryController right) {
		this(left, right, BOTH);
	}
	public final GridBagLayout layout;
	public final GridBagConstraints constraints;
	private final JSpinner spinner;
	/**
	 * Creates a item movement panel with specificallowed movement directions
	 * @param left the left inventory controller
	 * @param right the right inventory controller
	 * @param i byte tag of sides. Use {@link #NONE}, {@link #LEFT}, {@link #RIGHT} or {@link #BOTH}
	 * @throws NullPointerException when any inventory controller is {@code null}
	 */
	public MoveItems(AbstractInventoryController left, AbstractInventoryController right, int i) {
		Objects.requireNonNull(left, "left is null");
		Objects.requireNonNull(right, "right is null");
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.weightx = 1;
		setLayout(layout);
		//Moving left
		if((i & LEFT) != 0) {
			int itype = left.getInvType().ordinal();
			JButton btnAllL = new JButton("<< "+ALL);
			btnAllL.addActionListener(e -> {
				Inventories.transfer(right.getInv(), left.getInv());
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnAllL, constraints);
			add(btnAllL);
			
			JButton btnSelL = new JButton("<< "+SEL);
			btnSelL.addActionListener(e -> {
				for(ItemRecord selRecord: right.getSelectedValuesList()) {
					Inventories.transfer(selRecord, left.getInv());
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnSelL, constraints);
			add(btnSelL);
			
			JButton btnOPSelL = new JButton("<< "+OSEL);
			btnOPSelL.addActionListener(e -> {
				for(ItemRecord selRecord: right.getSelectedValuesList()) {
					Inventories.transfer(selRecord, left.getInv(), 1);
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOPSelL, constraints);
			add(btnOPSelL);
			
			JButton btnOneL = new JButton("<< "+ONE);
			btnOneL.addActionListener(e -> {
				Inventories.transfer(right.getSelectedValue(), left.getInv(), 1);
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOneL, constraints);
			add(btnOneL);
			
			JButton btnNSelL = new JButton("<< "+NSEL);
			btnNSelL.addActionListener(e -> {
				int n = amount();
				for(ItemRecord selRecord: right.getSelectedValuesList()) {
					Inventories.transfer(selRecord, left.getInv(), n);
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnNSelL, constraints);
			add(btnNSelL);
			
			JButton btnNL = new JButton("<< "+N);
			btnNL.addActionListener(e -> {
				int n = amount();
				Inventories.transfer(right.getSelectedValue(), left.getInv(), n);
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnNL, constraints);
			add(btnNL);
		}
		
		//count
		spinner = new JSpinner();
		layout.setConstraints(spinner, constraints);
		add(spinner);
		
		//moving right
		if((i & RIGHT) != 0) {
			JButton btnNR = new JButton(N+" >>");
			btnNR.addActionListener(e -> {
				int n = amount();
				Inventories.transfer(left.getSelectedValue(), right.getInv(), n);
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnNR, constraints);
			add(btnNR);
			
			JButton btnNSelR = new JButton(NSEL+" >>");
			btnNSelR.addActionListener(e -> {
				int n = amount();
				for(ItemRecord selRecord: left.getSelectedValuesList()) {
					Inventories.transfer(selRecord, right.getInv(), n);
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnNSelR, constraints);
			add(btnNSelR);
			JButton btnOneR = new JButton(ONE+" >>");
			btnOneR.addActionListener(e -> {
				Inventories.transfer(left.getSelectedValue(), right.getInv(), 1);
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOneR, constraints);
			add(btnOneR);
			
			JButton btnOPSelR = new JButton(OSEL+" >>");
			btnOPSelR.addActionListener(e -> {
				for(ItemRecord selRecord: left.getSelectedValuesList()) {
					Inventories.transfer(selRecord, right.getInv(), 1);
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnOPSelR, constraints);
			add(btnOPSelR);
			
			JButton btnSelR = new JButton(SEL+" >>");
			btnSelR.addActionListener(e -> {
				for(ItemRecord selRecord: left.getSelectedValuesList()) {
					Inventories.transfer(selRecord, right.getInv());
				}
				left.refresh();
				right.refresh();
			});
			layout.setConstraints(btnSelR, constraints);
			add(btnSelR);
					
			JButton btnAllR = new JButton(ALL+" >>");
			btnAllR.addActionListener(e -> {
				Inventories.transfer(left.getInv(), right.getInv());
				left.refresh();
				right.refresh();
			});layout.setConstraints(btnAllR, constraints);
			add(btnAllR);
		}
	}
	/** @return requested amount of items to move */
	public int amount() {
		Integer selection = (Integer) spinner.getValue();
		if(selection == null) return 0;
		return selection.intValue();
	}
	public void addAdditionalComponent(Component comp) {
		layout.setConstraints(comp, constraints);
		add(comp);
	}
}
