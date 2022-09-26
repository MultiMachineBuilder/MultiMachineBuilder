/**
 * 
 */
package mmb.MENU.toolkit.events;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.Container;
import mmb.MENU.toolkit.Container.ComponentPoint;

/**
 * @author oskar
 *
 */
public class ContainerCEH implements ComponentEventHandler {
	Container cont;

	public ContainerCEH(Container cont) {
		super();
		this.cont = cont;
	}

	@Override
	public void drag(UIMouseEvent e) {
		ComponentPoint cp = cont.getComponentAndPoint(e.p);
		
		if(cp.component == null) return;
		ComponentEventHandler ceh = cp.component.getHandler();
		if(ceh == null) return;
		ceh.drag(new UIMouseEvent(cp.pos, e.button));
	}

	@Override
	public void press(UIMouseEvent e) {
		ComponentPoint cp = cont.getComponentAndPoint(e.p);
		if(cp.component == null) return;
		ComponentEventHandler ceh = cp.component.getHandler();
		if(ceh == null) return;
		ceh.press(new UIMouseEvent(cp.pos, e.button));
	}

	@Override
	public void release(UIMouseEvent e) {
		ComponentPoint cp = cont.getComponentAndPoint(e.p);
		if(cp.component == null) return;
		ComponentEventHandler ceh = cp.component.getHandler();
		if(ceh == null) return;
		ceh.release(new UIMouseEvent(cp.pos, e.button));
	}
	

}
