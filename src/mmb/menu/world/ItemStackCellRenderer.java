/**
 * 
 */
package mmb.menu.world;

import javax.swing.Icon;
import mmb.annotations.Nil;
import mmb.engine.recipe.ItemStack;

/**
 * Displays an item stack
 * @author oskar
 */
public class ItemStackCellRenderer extends GeneralItemCellRenderer<ItemStack>{
	/** The singleton instance of an item stack cell renderer */
	public static final ItemStackCellRenderer instance = new ItemStackCellRenderer();
	private static final long serialVersionUID = -3535344904857285958L;
	
	@Override
	public @Nil Icon getIcon(@Nil ItemStack item) {
		if(item == null) return null;
		return item.item().icon();
	}
	@Override
	public String getText(@Nil ItemStack item) {
		if(item == null) return "";
		return item.item.title();
	}
	@Override
	public boolean isStackEmpty(@Nil ItemStack item) {
		if(item == null) return true;
		return item.amount == 0;
	}

}