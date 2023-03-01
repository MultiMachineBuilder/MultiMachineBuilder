/**
 * 
 */
package mmb.engine.craft;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.NN;
import mmb.Nil;
import mmb.menu.world.craft.RecipeView;

/**
 * An implementation of cell renderer using a recipe view
 * @author oskar
 * @param <Tr> recipe type
 * @param <Tv> view type
 */
public class PlugAndPlayRecipeCellRenderer<@NN Tr, @NN Tv extends RecipeView<Tr>> implements ListCellRenderer<@NN Tr> {
	@NN private final Tv view;
	/**
	 * Creates a recipe renderer
	 * @param view recipe view
	 */
	public PlugAndPlayRecipeCellRenderer(Tv view) {
		this.view = view;
	}
	@Override
	public Component getListCellRendererComponent(@Nil JList<? extends Tr> list, Tr value, int index, boolean isSelected,
			boolean cellHasFocus) {
		view.set(value);
		return view;
	}

}
