/**
 * 
 */
package mmb.WORLD.gui.craft;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mmb.WORLD.crafting.recipes.CraftingRecipeGroup;
import mmb.WORLD.crafting.recipes.CraftingRecipeGroup.CraftingRecipe;
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
public class CraftingRecipeList extends JScrollPane {
	private static final long serialVersionUID = 828762086772542204L;
	
	public CraftingRecipeList(CraftingRecipeGroup group) {
		JList<RecipeEntry> list = new JList<>();
			RecipeEntry[] data = new RecipeEntry[group.recipes.size()];
			int i = 0;
			for(CraftingRecipe recipe: group.recipes) {
				data[i] = new RecipeEntry(recipe);
				i++;
			}
			list.setListData(data);
			list.setCellRenderer(new CellRenderer());
		setViewportView(list);
	}
	static class RecipeEntry{
		@Nonnull public final CraftingRecipe recipe;
		@Nonnull public final ItemStack[] stackO;
		@Nonnull public final ItemStack[] stackI;
		public RecipeEntry(CraftingRecipe recipe) {
			this.recipe = recipe;
			stackO = SimpleRecipeView.list2arr(recipe.out);
			stackI = SimpleRecipeView.list2arr(recipe.in);
		}
	}
	class CellRenderer extends CraftingRecipeView implements ListCellRenderer<RecipeEntry>{
		private static final long serialVersionUID = -3521037620010787027L;
		public CellRenderer() {
			setPreferredSize(new Dimension(600, 200));
		}
		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends RecipeEntry> list, @Nullable RecipeEntry value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if(value != null) set(value.recipe, value.stackO, value.stackI);
			return this;
		} 
	}
}
