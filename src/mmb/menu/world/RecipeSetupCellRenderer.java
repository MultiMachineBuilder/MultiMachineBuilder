package mmb.menu.world;

import javax.swing.Icon;

import mmb.annotations.Nil;
import mmb.engine.recipe2.RecipeSetup;

/**
 * Displays a recipe setup
 */
public class RecipeSetupCellRenderer extends GeneralItemCellRenderer<RecipeSetup>{
	private static final long serialVersionUID = 4244635293741432093L;
	public static final RecipeSetupCellRenderer INSTANCE = new RecipeSetupCellRenderer();

	@Override
	public @Nil Icon getIcon(@Nil RecipeSetup row) {
		if(row == null) return null;
		var item = row.catalyst();
		if(item == null) return null;
		return item.icon();
	}

	@Override
	public String getText(@Nil RecipeSetup row) {
		if(row == null) return "none";
		var item = row.catalyst();
		var word = row.craftWord();
		var code = row.craftCode();
		
		if(item == null) return "none";
		return item.title() + "\n" + code + " " + word;
	}

	@Override
	public boolean isStackEmpty(@Nil RecipeSetup item) {
		return false;
	}

}
