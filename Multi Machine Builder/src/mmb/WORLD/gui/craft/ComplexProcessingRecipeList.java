/**
 * 
 */
package mmb.WORLD.gui.craft;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup.ComplexProcessingRecipe;
import mmb.WORLD.inventory.ItemStack;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JList;

/**
 * @author oskar
 *
 */
public class ComplexProcessingRecipeList extends JScrollPane {
	private static final long serialVersionUID = 828762086772542204L;
	
	public ComplexProcessingRecipeList(ComplexProcessingRecipeGroup group) {
		JList<RecipeEntry> list = new JList<>();
			RecipeEntry[] data = new RecipeEntry[group.recipes.size()];
			int i = 0;
			for(ComplexProcessingRecipe recipe: group.recipes) {
				data[i] = new RecipeEntry(recipe);
				i++;
			}
			list.setListData(data);
			list.setCellRenderer(new CellRenderer());
		setViewportView(list);
	}
	static class RecipeEntry{
		@Nonnull public final ComplexProcessingRecipe recipe;
		@Nonnull public final Vector<ItemStack> stackI;
		@Nonnull public final Vector<ItemStack> stackO;
		public RecipeEntry(ComplexProcessingRecipe recipe) {
			this.recipe = recipe;
			stackI = SimpleRecipeView.list2vector(recipe.input);
			stackO = SimpleRecipeView.list2vector(recipe.output);
		}
	}
	class CellRenderer extends ComplexRecipeView implements ListCellRenderer<RecipeEntry>{
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
