package mmb.MENU.main;

import java.awt.*;
import javax.swing.*;

import mmb.DATA.contents.GameContents;
import mmb.debug.Debugger;
import mmb.testing.Patch9Test;
import mmb.ui.ExternalMods;
import mmb.ui.shop.ModShop;

import java.awt.event.ActionListener;
import java.net.URI;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import java.awt.Desktop;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import mmb.MENU.ItemLabel;
import mmb.WORLD.tileworld.DrawerPlainColor;

public class MainMenu extends JFrame {
	//ToolKit API
	public static void addTab(JPanel tab, String name) {
		mm.tabbedPane.add(name, tab);
	}
	public static void addToolBarEntry(JMenu menu) {
		mm.menuBar.add(menu);
	}
	public static void addMenu(JPanel menu, String name) {
		mm.getContentPane().add(name, menu);
	}
	
	protected JMenuBar menuBar;
	protected JTabbedPane tabbedPane;
	private final static Debugger debug = new Debugger("Main menu");
	private final JPanel contentPane;
	public final static String CARD_MENU = "mainMenu";
	public final static String CARD_MODS = "modList";
	public final static String CARD_SAVES = "saveList";
	public final static String GITHUB = "https://github.com/MultiMachineBuilder/MultiMachineBuilder";
	private boolean FullScreen = false;
	private static MainMenu mm;
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];

	/**
	 * Launch the application.
	 */
	public static void running() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					mm = new MainMenu();
					mm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setTitle("MultiMachineBuilder - "+GameContents.addons.size()+" mods");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Window");
		menuBar.add(mnNewMenu);
		
		JCheckBoxMenuItem stngFullScreen = new JCheckBoxMenuItem("FullScreen");
		stngFullScreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFullScreen(stngFullScreen.isSelected());
			}
		});
		mnNewMenu.add(stngFullScreen);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		
		JPanel mainMenu = new JPanel();
		contentPane.add(mainMenu, CARD_MENU);
		mainMenu.setLayout(new BorderLayout(0, 0));
		
		JPanel aside = new JPanel();
		mainMenu.add(aside, BorderLayout.WEST);
		aside.setLayout(new MigLayout("", "[]", "[][][][][][][][][][][]"));
		
		JButton btnSettings = new JButton("Settings");
		aside.add(btnSettings, "cell 0 0");
		
		JButton btnDownloadContent = new JButton("Download content");
		aside.add(btnDownloadContent, "cell 0 1");
		btnDownloadContent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ModShop().setVisible(true);
			}
		});
		
		JButton btnConfigureExternalContent = new JButton("Configure external content");
		aside.add(btnConfigureExternalContent, "cell 0 2");
		btnConfigureExternalContent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ExternalMods().setVisible(true);
			}
		});
		
		JButton btnWebsite = new JButton("Website");
		aside.add(btnWebsite, "cell 0 3");
		btnWebsite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URI(GITHUB));
				} catch (Exception e) {
					debug.pstm(e, "Unable to open GitHub");
				}
			}
		});
		
		JButton btnAgecontentLimits = new JButton("Age/content limits");
		aside.add(btnAgecontentLimits, "cell 0 4");
		
		JButton btnCredits = new JButton("Credits");
		aside.add(btnCredits, "cell 0 5");
		
		JButton btnLogIn = new JButton("Log in");
		aside.add(btnLogIn, "cell 0 6");
		btnLogIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JButton btnExit = new JButton("Exit");
		aside.add(btnExit, "cell 0 7");
		
		JButton btnNewButton = new JButton("TEST WINDOW");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Patch9Test().setVisible(true);
			}
		});
		aside.add(btnNewButton, "cell 0 9");
		
		ItemLabel itemLabel = new ItemLabel(new DrawerPlainColor(Color.BLUE), "test");
		aside.add(itemLabel, "cell 0 10");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainMenu.add(tabbedPane);
		
		tabbedPane.addTab("Saves", null, new PanelSaves(), null);
		tabbedPane.addTab("Mods", null, new PanelMods(), null);
	}
	
	
	
	/**
	 * @return the fullScreen
	 */
	public boolean isFullScreen() {
		return FullScreen;
	}

	/**
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullScreen(boolean fullScreen) {
		if(fullScreen) {
			dispose();
			setUndecorated(true);
			setVisible(true);
			device.setFullScreenWindow(this);
			setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		}else {
			device.setFullScreenWindow(null);
			dispose();
			setUndecorated(false);
			setVisible(true);
		}
		FullScreen = fullScreen;
		
	}
	private void loginInfo() {
		//if(LoginInfo.loggedIn()) {
			
		//}
	}
}
