/**
 * 
 */
package mmb.world.crafting;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.menu.world.craft.RecipeView;

/**
 * @author oskar
 * An implementation of cell renderer using a recipe view
 */
public class PlugAndPlayRecipeCellRenderer<Tr, Tv extends RecipeView<Tr>> implements ListCellRenderer<Tr> {

	private final Tv view;
	
	public PlugAndPlayRecipeCellRenderer(Tv view) {
		this.view = view;
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends Tr> list, Tr value, int index, boolean isSelected,
			boolean cellHasFocus) {
		view.set(value);
		return view;
	}

}
