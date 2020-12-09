/**
 * 
 */
package mmb.MENU.toolkit.menus;

import java.util.function.Consumer;

import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.components.ComponentSource;

/**
 * @author oskar
 * Describes a menu entry
 */
public final class Menu implements ComponentSource {
	public String name;
	public UIComponent contents;
	public ComponentSource generator;
	public Consumer<Menu> fromPrevious = (m) -> {};
	public Runnable onEntrance = () -> {};
	public Runnable onExit = () -> {};
	public Menu previous;
	
	public Menu(String name,  ComponentSource contents) {
		this.name = name;
		this.generator = contents;
	}
	public void onExit() {
		contents.destroy();
		onExit.run();
	}
	public void createNew() {
		
	}
	
	@Override
	public UIComponent getComponent() {
		return contents;
	}
}