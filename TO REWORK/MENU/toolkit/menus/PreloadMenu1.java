/**
 * 
 */
package mmb.MENU.toolkit.menus;

import mmb.MENU.toolkit.components.ComponentGenerator;

/**
 * @author oskar
 * A 1-argumnt menu generator preloaded with one argument.
 */
public class PreloadMenu1<T> extends MenuGenerator1<T> {
	public T arg0;
	/**
	 * @param name
	 * @param cg
	 */
	public PreloadMenu1(String name, ComponentGenerator<T> cg) {
		super(name, cg);
	}

}
