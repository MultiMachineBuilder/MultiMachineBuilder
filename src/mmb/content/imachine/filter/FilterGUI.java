/**
 * 
 */
package mmb.content.imachine.filter;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import mmb.Nil;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * Configures filters in blocks which implement {@link ControllableFilter}
 * @author oskar
 */
public class FilterGUI extends GUITab {
	private static final long serialVersionUID = 1L;
	private final Box box;
	private final InventoryController invctrl;
	private final ControllableFilter filter;
	/**
	 * Creates a filter configuration GUI
	 * @param filter filter block
	 * @param window world window
	 */
	public FilterGUI(ControllableFilter filter, WorldWindow window) {		
		
		this.filter = filter;
		setLayout(new MigLayout("", "[258px][]", "[155px][]"));
		
		//1st column: player inventory
		invctrl = new InventoryController();
		window.playerInventory(invctrl);
		add(invctrl, "cell 0 0,alignx left,aligny top");
		
		//2nd column: filters
		box = new Box(BoxLayout.Y_AXIS);
		add(box, "cell 1 0,alignx left,aligny top");
		
		SingleItemInventory[] invs = filter.getFilters();
		String[] titles = filter.getTitles();
		if(titles == null) {
			//Without titles
			String title = $res("wgui-filter");
			for(int i = 0; i < invs.length; i++) {
				addFilter(title+" #"+(i+1), invs[i]);
			}
		}else {
			//With titles
			if(titles.length != invs.length)
				throw new IllegalStateException("The number of titles: "+titles.length+" does not match number of filters: "+invs.length);
			for(int i = 0; i < titles.length; i++) {
				addFilter(titles[i], invs[i]);
			}
		}
		
		//Exit button
		JButton btnExit = new JButton($res("exit"));
		btnExit.setBackground(Color.RED);
		add(btnExit, "cell 0 1 2 1,growx");
		btnExit.addActionListener(e -> window.closeWindow(this));
	}
	private void addFilter(String title, SingleItemInventory inv) {
		Box sub = new Box(BoxLayout.X_AXIS);
		SingleInventoryController ctrl = new SingleInventoryController(inv);
		sub.add(new MoveItems(invctrl, ctrl));
		sub.add(ctrl);
		box.add(sub);
	}
	@Override
	public void close(WorldWindow window) {
		filter.destroyTab(this);
	}

}
