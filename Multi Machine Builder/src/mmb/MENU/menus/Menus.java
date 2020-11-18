/**
 * 
 */
package mmb.MENU.menus;

import java.awt.Color;
import java.util.List;
import java.util.Stack;

import mmb.MENU.GameWindow;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.components.UIButton;
import mmb.MENU.toolkit.components.UILayoutLinear;
import mmb.MENU.toolkit.components.UILayoutLinear.Axis;
import mmb.MENU.toolkit.components.UITextScrollist;
import mmb.MENU.toolkit.menus.Menu;

/**
 * @author oskar
 *
 */
public class Menus {
	public static Menu mainMenu, playMenu; 
	
	static {
		mainMenu = new Menu("Main Menu", mainMenu());
		playMenu = new Menu("SELECT A SAVE", playMenu());
		playMenu.previous = mainMenu;
	}
	
	private static UIComponent mainMenu() {
		//play game
		UIButton btnPlay = new UIButton("Play",() -> {
			GameWindow.uimn.setMenu(playMenu);
		});
		btnPlay.background = Color.GREEN;
		
		return new UILayoutLinear(Axis.VERTICAL, btnPlay);
	}
	
	private static UIComponent playMenu() {
		UILayoutLinear uill = new UILayoutLinear();
		uill.direction = Axis.VERTICAL;
		uill.spacing = 0;
		
		//save scrollist
		UITextScrollist uitscrl = new UITextScrollist();
		
		//populate the scrollist
		uitscrl.add("Test0");
		uitscrl.add("Test1");
		uitscrl.add("Test2");
		uitscrl.add("Test3");
		
		//refresh
		UIButton btnReset = new UIButton("Refresh", () -> {
			
		});
		btnReset.constantHeight(16);
		
		UIButton btnPlay = new UIButton("Play", () -> {
			
		});
		btnPlay.background = new Color(20, 200, 20);
		btnPlay.constantHeight(24);
		uill.contents = new UIComponent[] {uitscrl, btnReset, btnPlay};
		return uill;
	}
}
