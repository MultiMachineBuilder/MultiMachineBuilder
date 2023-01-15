/**
 * 
 */
package mmb.content.modular.chest;

import javax.swing.JPanel;

import mmb.content.modular.gui.SafeCloseable;
import mmb.menu.world.inv.InventoryController;
import java.awt.BorderLayout;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.inv.AbstractInventoryController;

/**
 *
 * @author oskar
 *
 */
public class ChestCoreAccess extends JPanel implements SafeCloseable {
	private static final long serialVersionUID = -1462133036548585770L;

	public ChestCoreAccess(InventoryController invctrl, ChestCore<?> core) {
		setLayout(new BorderLayout(0, 0));
		
		InventoryController invctrl2 = new InventoryController(core.inventory);
		add(invctrl2, BorderLayout.CENTER);
		
		MoveItems moveItems = new MoveItems(invctrl, invctrl2);
		add(moveItems, BorderLayout.WEST);
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
