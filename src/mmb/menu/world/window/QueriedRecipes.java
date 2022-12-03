/**
 * 
 */
package mmb.menu.world.window;

import java.awt.Component;
import java.awt.Dimension;
import javax.annotation.Nullable;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import mmb.menu.world.CellRendererGettingCellRenderer;
import mmb.menu.world.window.TabInventory.RecipeQuery;
import mmb.world.crafting.Recipe;
import monniasza.collects.Collects;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;

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
		setLayout(new BorderLayout(0, 0));
		
		
		JButton btnNewButton = new JButton("Exit");
		btnNewButton.setBackground(Color.RED);
		btnNewButton.addActionListener(e -> window.closeWindow(this));
		add(btnNewButton, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		JList<Recipe<?>> list = new JList<>();
		scrollPane.setViewportView(list);
		
		//Populate the list
		ListModel<Recipe<?>> model = Collects.newListModel(query.eligible());

		JLabel lblNewLabel = new JLabel("["+model.getSize()+"]Filter: "+query.name());
		add(lblNewLabel, BorderLayout.NORTH);
		list.setCellRenderer(new CellRendererGettingCellRenderer<>(QueriedRecipes::getcellrend));
		list.setModel(model);
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
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends Component> list, @Nullable Component value, int index,
				boolean isSelected, boolean cellHasFocus) {
			return value;
		} 
	}

}
