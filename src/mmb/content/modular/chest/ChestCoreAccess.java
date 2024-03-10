/**
 * 
 */
package mmb.content.modular.chest;

import javax.swing.JPanel;

import mmb.NN;
import mmb.content.modular.gui.SafeCloseable;
import mmb.menu.world.inv.InventoryController;
import java.awt.BorderLayout;
import mmb.menu.world.inv.MoveItems;

/**
 *
 * @author oskar
 *
 */
public class ChestCoreAccess extends JPanel implements SafeCloseable {
	private static final long serialVersionUID = -1462133036548585770L;
	@NN private final InventoryController invctrl2;

	public ChestCoreAccess(InventoryController invctrl, ChestCore core) {
		setLayout(new BorderLayout(0, 0));
		
		invctrl2 = new InventoryController(core.inventory);
		add(invctrl2, BorderLayout.CENTER);
		
		MoveItems moveItems = new MoveItems(invctrl, invctrl2);
		add(moveItems, BorderLayout.WEST);
	}
	
	@Override
	public void close() {
		//unused
	}
}
