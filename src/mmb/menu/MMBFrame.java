/**
 * 
 */
package mmb.menu;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.net.URI;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import mmb.NN;
import mmb.engine.debug.Debugger;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;
import mmb.menu.components.BoundCheckBoxMenuItem;

/**
 * An auxiliary frame class, which supports full screen control
 * @author oskar
 */
@SuppressWarnings("serial")
public abstract class MMBFrame extends JFrame {
	//Logos
	/** The gear icon */
	public static final BufferedImage GEAR = Textures.get("gearlogo.png");
	/** The dollar icon */
	public static final BufferedImage DOLLAR = Textures.get("dollar.png");
	/** The bug report icon */
	public static final BufferedImage BUG = Textures.get("bug.png");
	
	protected MMBFrame() {
		setJMenuBar(menuBar);
		mnWindow = new JMenu($res("cgui-win"));
		
		//Report a bug
		mnBug = new JMenuItem(GlobalSettings.$res("cgui-report"), new ImageIcon(BUG));
		mnBug.setBackground(new Color(255, 255, 0));
		mnWindow.add(mnBug);
		mnBug.setModel(MenuHelper.btnmBug);
		
		//Full screen toggle
		stngFullScreen = new BoundCheckBoxMenuItem($res("wgui-fulls"));
		stngFullScreen.setBackground(new Color(192, 192, 192));
		stngFullScreen.setVariable(FullScreen.isFullScreen);
		mnWindow.add(stngFullScreen);
		
		//To main menu
		JMenuItem mntmMMenu = new JMenuItem($res("wgui-main"));
		mntmMMenu.setBackground(Color.ORANGE);
		mntmMMenu.addActionListener(e -> dispose());
		mnWindow.add(mntmMMenu);
		//To desktop
		JMenuItem mntmExitDesktop = new JMenuItem($res("wgui-dtp"));
		mntmExitDesktop.setBackground(Color.RED);
		mntmExitDesktop.addActionListener(e -> {
			dispose();
			System.exit(0);
		});
		mnWindow.add(mntmExitDesktop);
		
		//Ukrainian flags
		Icon ua0 = new ImageIcon(Textures.get("ukraine.png"));
		Icon ua1 = new ImageIcon(Textures.get("ukraine1.png"));
		
		//Donate to help Ukraine
		JMenuItem btnUkraine = new JMenuItem($res("cgui-refugeedonate"));
		mnWindow.add(btnUkraine);
		btnUkraine.setIcon(ua0);
		btnUkraine.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(helpUA));
			} catch (Exception ex) {
				debug.stacktraceError(ex, "Unable to help Ukrainian refugees");
			}
		});
		
		//This button is for English speakers. It leads to a site for refugees.
		JMenuItem btnRefugee = new JMenuItem($res("cgui-refugeehelp"));
		mnWindow.add(btnRefugee);
		btnRefugee.setIcon(ua1);
		btnRefugee.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(refugeesEN)); //An English-language site for Ukrainian refugees
			} catch (Exception ex) {
				debug.stacktraceError(ex, "Unable to get help as a refugee");
			}
		});
		
		//This button is for Ukrainian speakers. It means the same thing as the English one
		JMenuItem btnRefugee1 = new JMenuItem(GlobalSettings.$res("ua-refugees"));
		mnWindow.add(btnRefugee1);
		btnRefugee1.setIcon(ua1);
		btnRefugee1.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(refugeesUA)); //An Ukrainian-language site for Ukrainian refugees
			} catch (Exception ex) {
				debug.stacktraceError(ex, GlobalSettings.$res("ua-refugee-error"));
			}
		});
	}
	
	//Menu
	public void addMenu(Component comp) {
		menuBar.add(comp);
	}
	public void removeMenu(Component comp) {
		menuBar.remove(comp);
	}
	@NN protected final JMenuBar menuBar = new JMenuBar();
	@NN protected final JMenu mnWindow;
	private JMenuItem mnBug;
	/** Donate to help Ukrainian refugees */
	private static final String helpUA = "https://good.od.ua/en/stopwar";
	//https://www.gov.pl/web/mswia-pl/informacja-dla-uchodżców-z-ukrainy
	/** English information for Ukrainian refugees */
	private static final String refugeesEN = "https://www.gov.pl/web/mswia-en/information-for-refugees-from-ukraine";
	/** Polish government services for Ukrainian refugees*/
	private static final String refugeesUA = "http://www.ua.gov.pl/";
	private final BoundCheckBoxMenuItem stngFullScreen;
	private static final Debugger debug = new Debugger("Main menu");
	
	
	//Dispose management
	boolean undergoingScreenTransform = false;
	/**
	 * Check if the screen is currently undergoing a full-screen transform. It serves to disable destroy() method when using dispose();
	 * @return the undergoingScreenTransform
	 */
	public boolean isUndergoingScreenTransform() {
		return undergoingScreenTransform;
	}
	boolean isDisposing;
	
	@Override
	public synchronized void dispose() {
		if(isDisposing) return;
		try {
			isDisposing = true;
			if(!undergoingScreenTransform) destroy();
			super.dispose();
		}finally {
			isDisposing = false;
		}
		
	}
	/**
	 * Destroy any involved data, resetting for next set-up
	 */
	public abstract void destroy();
}
