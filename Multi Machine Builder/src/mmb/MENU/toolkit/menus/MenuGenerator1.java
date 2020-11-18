/**
 * 
 */
package mmb.MENU.toolkit.menus;

import java.util.function.Consumer;

import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.components.ComponentGenerator;
import mmb.MENU.toolkit.components.ComponentSource;

/**
 * @author oskar
 * @param <T> type of generator's first argument
 *
 */
public class MenuGenerator1<T> implements MenuSource {
	public final String name;
	public final ComponentGenerator<T> contents;
	public UIComponent lastComponent;
	public Consumer<Menu> fromPrevious = (m) -> {};
	public Runnable onEntrance = () -> {};
	public Runnable onExit = () -> {};
	public MenuSource waitingScreen;
	public MenuGenerator1(String name, ComponentGenerator<T> cg) {
		this.name = name;
		this.contents = cg;
	}
	public void onExit() {
		lastComponent.destroy();
		onExit.run();
	}
	public Menu previous;
	@Override
	public UIComponent getComponent() {
		return lastComponent;
	}
	/**
	 * Generate a new menu
	 * @param arg0
	 * @return
	 */
	public UIComponent generateNew(T arg0) {
		return lastComponent = contents.getComponent(arg0);
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public Runnable getLoadHandler() {
		return onEntrance;
	}
	@Override
	public Runnable getExitHandler() {
		return onExit;
	}
	@Override
	public MenuSource getWaitingScreen() {
		return waitingScreen;
	}
}