/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.util.function.Supplier;

import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 * A component source. Components are sources themselves.
 * Menus are sources;
 */
@FunctionalInterface
public interface ComponentSource {
	public UIComponent getComponent();
	
	public static ComponentSource fromSupplier(Supplier<UIComponent> uic) {
		return new ComponentSource() {
			@Override public UIComponent getComponent() {return uic.get();}
		};
	}
}
