/**
 * 
 */
package mmb.menu.world.craft;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mmb.world.recipes.PickaxeGroup;
import mmb.world.recipes.PickaxeGroup.PickaxeInfo;

import java.awt.Component;
import java.awt.Dimension;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JList;

/**
 * @author oskar
 */
public class PickaxeList extends JScrollPane {
	private static final long serialVersionUID = 828762086772542204L;
	private final PickaxeView rv;
	/**
	 * Creates a list of pickaxe recipes
	 * @param group recipe group for this list
	 * */
	public PickaxeList(PickaxeGroup group) {
		rv = new PickaxeView();
		JList<RecipeEntry> list = new JList<>();
		
			@SuppressWarnings("unchecked")
			RecipeEntry[] data = new RecipeEntry[group.recipes().size()];
			int i = 0;
			for(PickaxeInfo recipe: group.recipes()) {
				data[i] = new RecipeEntry(recipe);
				i++;
			}
			list.setListData(data);
			list.setCellRenderer(new CellRenderer());
		setViewportView(list);
	}
	static class RecipeEntry{
		@Nonnull public final PickaxeInfo recipe;
		public RecipeEntry(PickaxeInfo recipe2) {
			this.recipe = recipe2;
		}
	}
	class CellRenderer implements ListCellRenderer<RecipeEntry>{
		public CellRenderer() {
			setPreferredSize(new Dimension(600, 200));
		}
		@Override
		public Component getListCellRendererComponent(
				@SuppressWarnings("null") JList<? extends RecipeEntry> list,
						@Nullable RecipeEntry value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if(value != null) rv.set(value.recipe);
			return rv;
		} 
	}
}
