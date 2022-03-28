/**
 * 
 */
package mmb.WORLD.gui.craft;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mmb.WORLD.crafting.recipes.ComplexCatalyzedProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexCatalyzedProcessingRecipeGroup.ComplexCatalyzedProcessingRecipe;
import mmb.WORLD.inventory.ItemStack;

import java.awt.Component;
import java.awt.Dimension;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JList;

/**
 * @author oskar
 *
 */
public class ComplexCatalyzedProcessingRecipeList extends JScrollPane {
	private static final long serialVersionUID = 828762086772542204L;
	
	public ComplexCatalyzedProcessingRecipeList(ComplexCatalyzedProcessingRecipeGroup group) {
		JList<RecipeEntry> list = new JList<>();
			RecipeEntry[] data = new RecipeEntry[group.recipes.size()];
			int i = 0;
			for(ComplexCatalyzedProcessingRecipe recipe: group.recipes) {
				data[i] = new RecipeEntry(recipe);
				i++;
			}
			list.setListData(data);
			list.setCellRenderer(new CellRenderer());
		setViewportView(list);
	}
	static class RecipeEntry{
		@Nonnull public final ComplexCatalyzedProcessingRecipe recipe;
		@Nonnull public final ItemStack[] stackI;
		@Nonnull public final ItemStack[] stackO;
		public RecipeEntry(ComplexCatalyzedProcessingRecipe recipe) {
			this.recipe = recipe;
			stackI = SimpleRecipeView.list2arr(recipe.input);
			stackO = SimpleRecipeView.list2arr(recipe.output);
		}
	}
	class CellRenderer extends ComplexCatalyzedRecipeView implements ListCellRenderer<RecipeEntry>{
		private static final long serialVersionUID = -3521037620010787027L;
		public CellRenderer() {
			setPreferredSize(new Dimension(600, 200));
		}
		@Override
		public Component getListCellRendererComponent(JList<? extends RecipeEntry> list, @Nullable RecipeEntry value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if(value != null) set(value.recipe, value.stackO, value.stackI);
			return this;
		} 
	}
}
