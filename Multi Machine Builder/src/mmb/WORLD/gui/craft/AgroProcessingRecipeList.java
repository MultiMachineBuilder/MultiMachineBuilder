/**
 * 
 */
package mmb.WORLD.gui.craft;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mmb.WORLD.crafting.recipes.AgroRecipeGroup;
import mmb.WORLD.crafting.recipes.AgroRecipeGroup.AgroProcessingRecipe;
import mmb.WORLD.crafting.recipes.ElectroSimpleProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ElectroSimpleProcessingRecipeGroup.ElectroSimpleProcessingRecipe;
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
public class AgroProcessingRecipeList extends JScrollPane {
	private static final long serialVersionUID = 828762086772542204L;
	
	public AgroProcessingRecipeList(AgroRecipeGroup group) {
		JList<RecipeEntry> list = new JList<>();
			RecipeEntry[] data = new RecipeEntry[group.recipes.size()];
			int i = 0;
			for(AgroProcessingRecipe recipe: group.recipes) {
				data[i] = new RecipeEntry(recipe);
				i++;
			}
			list.setListData(data);
			list.setCellRenderer(new CellRenderer());
		setViewportView(list);
	}
	static class RecipeEntry{
		@Nonnull public final AgroProcessingRecipe recipe;
		@Nonnull public final ItemStack[] stack;
		public RecipeEntry(AgroProcessingRecipe recipe2) {
			this.recipe = recipe2;
			stack = SimpleRecipeView.list2arr(recipe2.output);
		}
	}
	class CellRenderer extends AgroRecipeView implements ListCellRenderer<RecipeEntry>{
		private static final long serialVersionUID = -3521037620010787027L;
		public CellRenderer() {
			setPreferredSize(new Dimension(600, 200));
		}
		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("null") JList<? extends RecipeEntry> list, @Nullable RecipeEntry value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if(value != null) set(value.recipe, value.stack);
			return this;
		} 
	}
}
