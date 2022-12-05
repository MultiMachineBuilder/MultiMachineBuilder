/**
 * 
 */
package mmb.menu.world.craft;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mmbeng.craft.Recipe;
import mmbeng.craft.RecipeGroup;
import mmbeng.inv.ItemStack;

import java.awt.Component;
import java.awt.Dimension;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JList;

/**
 * Represents a recipe list for almost anything (but not pickaxes)
 * @author oskar
 * @param <T> type of the recipes
 * @param <G> type of the recipe group
 */
public class RecipeList<T extends Recipe<T>, G extends RecipeGroup<T>> extends JScrollPane {
	private static final long serialVersionUID = 828762086772542204L;
	private final RecipeView<T> rv;
	/**
	 * Creates a recipe list for almost anything (but not pickaxes)
	 * @param group recipe group used for this list
	 * */
	public RecipeList(G group) {
		rv = group.createView();
		JList<RecipeEntry<T>> list = new JList<>();
		
			@SuppressWarnings("unchecked")
			RecipeEntry<T>[] data = new RecipeEntry[group.recipes().size()];
			int i = 0;
			for(T recipe: group.recipes()) {
				data[i] = new RecipeEntry<>(recipe);
				i++;
			}
			list.setListData(data);
			list.setCellRenderer(new CellRenderer());
		setViewportView(list);
	}
	static class RecipeEntry<T extends Recipe<T>>{
		@Nonnull public final T recipe;
		@Nonnull public final ItemStack[] stack;
		public RecipeEntry(T recipe2) {
			this.recipe = recipe2;
			stack = VectorUtils.list2arr(recipe2.output());
		}
	}
	class CellRenderer implements ListCellRenderer<RecipeEntry<T>>{
		public CellRenderer() {
			setPreferredSize(new Dimension(600, 200));
		}
		@Override
		public Component getListCellRendererComponent(
				@SuppressWarnings("null") JList<? extends RecipeEntry<T>> list,
						@Nullable RecipeEntry<T> value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if(value != null) rv.set(value.recipe);
			return rv;
		} 
	}
}
