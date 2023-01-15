/**
 * 
 */
package mmb.menu.world.window;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import mmb.Nil;
import mmb.engine.craft.Recipe;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.components.CellRendererGettingCellRenderer;
import mmb.menu.world.window.TabInventory.RecipeQuery;
import monniasza.collects.Collects;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class QueriedRecipes extends GUITab {
	private static final long serialVersionUID = 2696208326215735995L;

	/**
	 * Create the panel.
	 * @param query the query to search recipes
	 * @param window world window
	 */
	public QueriedRecipes(RecipeQuery query, WorldWindow window) {
		
		
		JButton btnNewButton = new JButton(GlobalSettings.$res("exit"));
		btnNewButton.setBackground(Color.RED);
		btnNewButton.addActionListener(e -> window.closeWindow(this));
		setLayout(new MigLayout("", "[450px,grow]", "[13px][266px,grow][21px]"));
		add(btnNewButton, "flowx,cell 0 2,growx,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1,grow");
		
		JList<Recipe<?>> list = new JList<>();
		scrollPane.setViewportView(list);
		
		//Populate the list
		ListModel<Recipe<?>> model = Collects.newListModel(query.eligible());

		JLabel lblNewLabel = new JLabel("["+model.getSize()+"]Filter: "+query.name());
		add(lblNewLabel, "cell 0 0,growx,aligny top");
		list.setCellRenderer(new CellRendererGettingCellRenderer<>(QueriedRecipes::getcellrend));
		list.setModel(model);
		
		JButton alone = new JButton(GlobalSettings.$res("wguicf-alone"));
		alone.setBackground(new Color(0, 191, 255));
		alone.addActionListener(e -> {
			Recipe recipe = list.getSelectedValue();
			RecipeQuery query2 = TabInventory.alone(Set.of(recipe));
			QueriedRecipes qr2 = new QueriedRecipes(query2, window);
			window.openAndShowWindow(qr2, GlobalSettings.$res("wguicf-alone2"));
			window.closeWindow(this);
		});
		add(alone, "cell 0 2");
	}
	
	@SuppressWarnings("unchecked")
	private static ListCellRenderer<Recipe<?>> getcellrend(Recipe<?> recipe) {
		return (ListCellRenderer<Recipe<?>>) recipe.group().cellRenderer();
	}

	@Override
	public void close(WorldWindow window) {
		// unused
	}
	
	class CellRenderer implements ListCellRenderer<Component>{
		public CellRenderer() {
			setPreferredSize(new Dimension(600, 200));
		}
		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends Component> list, @Nil Component value, int index,
				boolean isSelected, boolean cellHasFocus) {
			return value;
		} 
	}

}
