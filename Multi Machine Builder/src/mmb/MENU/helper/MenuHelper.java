/**
 * 
 */
package mmb.MENU.helper;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

/**
 * @author oskar
 *
 */
public class MenuHelper {
	private MenuHelper() {}
	/**
	 * @param strTrue
	 * @param strFalse
	 * @param tooltip
	 * @param message
	 * @param action
	 * @return
	 */
	public static BooleanDialog askBoolean(String strTrue, String strFalse, String tooltip, String message, BooleanConsumer action) {
		BooleanDialog result = new BooleanDialog(strTrue, strFalse, tooltip, message, action);
		result.setVisible(true);
		return result;
	}
}
