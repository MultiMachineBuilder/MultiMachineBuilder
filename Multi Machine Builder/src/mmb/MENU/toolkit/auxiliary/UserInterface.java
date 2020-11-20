/**
 * 
 */
package mmb.MENU.toolkit.auxiliary;

import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.components.ComponentGenerator;
import mmb.MENU.toolkit.components.ComponentSource;

/**
 * @author oskar
 * A class to help make user interfaces.
 */
public abstract class UserInterface<T> {
	protected T data;
	protected UIComponent contents;
	protected boolean requiresInput = false;
	//INTERFACE METHODS
	protected void onSetUp() {}
	protected void onExit() {}
	protected void afterExit() {}
	protected void onValueGiven(T o) {}
	
	protected void preLoad() {}
	protected void onLoad() {}
	protected void afterLoad() {}
	
	protected void beforeFrame() {}
	protected void afterFrame() {}
	protected T loadNewData() {return null;}
	
	//PUBLIC METHODS
	public UserInterface() {

	};
	public void create(){
		onSetUp();
		create(loadNewData());
	}
	public void create(T value) {
		onSetUp();
		create(value);
	}
	public void createWithoutValue() {
		onSetUp();
		create(null);
	}
	private void create0(T value) {
		if(value == null) {
			if(requiresInput) {
				throw null;
			}
			reload();
		}else {
			giveValue(value);
		}
	}
	public void destroy() {
		onExit();
		contents.destroy();
		afterExit();
	}
	public void giveValue(T o) {
		preLoad();
		onValueGiven(o);
		onLoad();
		afterLoad();
	}
	public void reload() {
		preLoad();
		onLoad();
		afterLoad();
	}
	public ComponentSource getSource() {
		return new ComponentSource() {
			@Override
			public UIComponent getComponent(){
				create();
				return contents;
			}
			
		};
	}
	public ComponentGenerator<T> getGenerator() {
		return new ComponentGenerator<T>() {

			@Override
			public UIComponent getComponent(T arg0) {
				create(arg0);
				return contents;
			}
			
			
		};
	}
	
	//AUXILIARY METHODS
	
	
}
