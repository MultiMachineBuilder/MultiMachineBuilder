/**
 * 
 */
package mmb.MENU.toolkit.events;

import mmb.MENU.toolkit.Container;
import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 *
 */
public class ContainerSEH implements SharedEventHandler {
	private Container cont;
	public ContainerSEH(Container cont) {
		super();
		this.cont = cont;
	}

	@Override
	public void keyPress(int key) {
		UIComponent[] comps = cont.getComponents();
		for(int i = 0; i < comps.length; i++) {
			if(comps[i].getCommonEventHandler() == null) continue;
			comps[i].getCommonEventHandler().keyPress(key);
		}
	}

	@Override
	public void keyRelease(int key) {
		UIComponent[] comps = cont.getComponents();
		for(int i = 0; i < comps.length; i++) {
			if(comps[i].getCommonEventHandler() == null) continue;
			comps[i].getCommonEventHandler().keyRelease(key);
		}
	}

	@Override
	public void scroll(int scroll) {
		UIComponent[] comps = cont.getComponents();
		for(int i = 0; i < comps.length; i++) {
			if(comps[i].getCommonEventHandler() == null) continue;
			comps[i].getCommonEventHandler().scroll(scroll);
		}
	}

}
