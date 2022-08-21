/**
 * 
 */
package mmb.WORLD.gui.window;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import io.vavr.Tuple2;

/**
 * @author oskar
 *
 */
public class TabRecipes extends JPanel {
	private static final long serialVersionUID = -2195639936444264957L;
	
	private static final List<Supplier<Tuple2<String, JComponent>>> tabs = new ArrayList<>();
	/**
	 * Adds a tab
	 * @param tab a function, which returns tab title and its component
	 */
	public static void add(Supplier<Tuple2<String, JComponent>> tab) {
		tabs.add(tab);
	}
	/**
	 * Create the panel.
	 */
	public TabRecipes() {
		setLayout(new MigLayout("", "[grow][]", "[][grow]"));
		
		JLabel lblNewLabel = new JLabel("Select a recipe group, or search using inventory");
		add(lblNewLabel, "flowx,cell 0 0,growx");
		
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
			for(Supplier<Tuple2<String, JComponent>> tabsup: tabs) {
				Tuple2<String, JComponent> result = tabsup.get();
				tabbedPane.add(result._1, result._2);
			}
		add(tabbedPane, "cell 0 1,grow");
		
	}

}
