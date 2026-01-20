/**
 * 
 */
package mmb.menu.world;

import javax.swing.Icon;

import mmb.annotations.Nil;
import mmb.engine.recipe3.OutputRow;

/**
 * Displays an item stack
 * @author oskar
 */
public class OutputRowCellRenderer extends GeneralItemCellRenderer<OutputRow>{
	/** The singleton instance of an item stack cell renderer */
	public static final OutputRowCellRenderer instance = new OutputRowCellRenderer();
	private static final long serialVersionUID = -833967396218582239L;
	
	@Override
	public @Nil Icon getIcon(@Nil OutputRow item) {
		if(item == null) return null;
		return item.item().icon();
	}
	@Override
	public String getText(@Nil OutputRow item) {
		if(item == null) return "";
		return item.tooltip();
	}
	@Override
	public boolean isStackEmpty(@Nil OutputRow item) {
		return item == null || item.amount() <= 0;
	}
}