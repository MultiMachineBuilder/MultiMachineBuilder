/**
 * 
 */
package mmb.menu.world;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import mmb.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class BOMEditor extends Box {
	private static final long serialVersionUID = -4109999231642937093L;

	/**
	 * Creates a BOM editor
	 * @param selector
	 */
	public BOMEditor(InventoryController selector) {
		super(BoxLayout.Y_AXIS);
		
		JButton btn = new JButton("+");
		btn.setBackground(Color.GREEN);
	}
}
