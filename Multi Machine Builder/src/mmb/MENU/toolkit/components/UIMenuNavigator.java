/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Color;
import java.awt.Graphics;
import mmb.MENU.toolkit.Container;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.auxiliary.Pane;
import mmb.MENU.toolkit.menus.Menu;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class UIMenuNavigator extends Container {
	public UIComponent extendedToolbar;
	public int toolbarHeight = 32;
	private Menu current;
	private Pane toolPane, contentPane, exitPane;
	private static Debugger debug = new Debugger("MENUS");
	private UIButton exitButton;
	/**
	 * 
	 */
	public UIMenuNavigator() {
		super();
		exitButton = new UIButton("<<<");
		exitButton.action = this::backward;
		exitButton.background = Color.RED;
	}

	@Override
	public void prepare(Graphics g) {
		//If there is no extended menu, fill top with go back
		int leftboundexit, exitwidth;
		if(extendedToolbar == null) {
			leftboundexit = 0;
			toolPane = null;
			exitwidth = getWidth();
		}else {
			leftboundexit= getWidth() - toolbarHeight;
			exitwidth = toolbarHeight;
			//Generate tool pane
			toolPane = new Pane(0, 0, extendedToolbar);
			toolPane.setSize(leftboundexit - 1, toolbarHeight);
		}
		
		//Generate exit pane
		exitPane = new Pane(leftboundexit, 0, exitButton);
		exitPane.setSize(exitwidth, toolbarHeight);
		exitPane.prepare(g);
		
		//Generate content
		contentPane = new Pane(0, toolbarHeight, current.contents);
		contentPane.setSize(getWidth(), getHeight() - toolbarHeight);
		contentPane.prepare(g);
		
		if(toolPane == null) {
			comppos = new Pane[] {exitPane, contentPane};
		}else {
			extendedToolbar.prepare(g);
			comppos = new Pane[] {exitPane, contentPane, toolPane};
		}
	}
	public void setMenu(Menu m) {
		debug.print("Going to ");
		debug.printl(m.name);
		if(current != null) current.onExit();
		m.fromPrevious.accept(current);
		m.onEntrance.run();
		current = m;
	}
	public void backward() {
		if(current == null || current.previous == null) {
			debug.printl("Trying to exit without a valid previous menu");
			return;
		}
		setMenu(current.previous);
	}
	public Menu getMenu() {
		return current;
	}

}
