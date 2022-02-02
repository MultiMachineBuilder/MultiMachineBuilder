/**
 * 
 */
package mmb.WORLD.gui.window;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.annotation.Nullable;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.recipes.GlobalRecipeRegistrar;
import mmb.WORLD.gui.craft.SimpleRecipeView;
import mmb.WORLD.gui.window.TabInventory.RecipeQuery;
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
		
		JList<Component> list = new JList<>();
		scrollPane.setViewportView(list);
		
		//Populate the list
		Vector<Component> vector = new Vector<>();
		for(Recipe<?> recipe: GlobalRecipeRegistrar.recipes) {
			if(query.filter(recipe)) {
				vector.add(recipe.createComponent());
			}
		}

		JLabel lblNewLabel = new JLabel("["+vector.size()+"]Filter: "+query.name());
		add(lblNewLabel, BorderLayout.NORTH);
		list.setCellRenderer(new CellRenderer());
		list.setListData(vector);
	}

	@Override
	public void createTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
	
	class CellRenderer extends SimpleRecipeView implements ListCellRenderer<Component>{
		private static final long serialVersionUID = 319982452315075438L;
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
