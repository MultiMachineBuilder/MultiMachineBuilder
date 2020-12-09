package mmb.ui.window;

import java.awt.*;
import javax.swing.*;

import mmb.DATA.contents.GameContents;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import mmb.WORLD.tileworld.TileGUI;
import mmb.debug.Debugger;
import mmb.ui.ExternalMods;
import mmb.ui.game.SelectGame;
import mmb.ui.game.WorldFrame;
import mmb.ui.shop.ModShop;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;

public class MainMenu extends JFrame {
	private final static Debugger debug = new Debugger("Main menu");
	private final JPanel contentPane;
	public final static String CARD_MENU = "mainMenu";
	public final static String CARD_MODS = "modList";
	public final static String CARD_SAVES = "saveList";
	public final static String GITHUB = "https://github.com/MultiMachineBuilder/MultiMachineBuilder";
	private boolean FullScreen = false;
	
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
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
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
		
		JMenuBar menuBar = new JMenuBar();
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
		aside.setLayout(new MigLayout("", "[]", "[][][][][][][][][][]"));
		
		JButton btnNewGame = new JButton("Play");
		aside.add(btnNewGame, "cell 0 0");
		btnNewGame.setForeground(Color.BLACK);
		btnNewGame.setBackground(Color.GREEN);
		
		JButton btnSettings = new JButton("Settings");
		aside.add(btnSettings, "cell 0 1");
		
		JButton btnDownloadContent = new JButton("Download content");
		aside.add(btnDownloadContent, "cell 0 3");
		
		JButton btnConfigureExternalContent = new JButton("Configure external content");
		aside.add(btnConfigureExternalContent, "cell 0 4");
		
		JButton btnWebsite = new JButton("Website");
		aside.add(btnWebsite, "cell 0 5");
		
		JButton btnAgecontentLimits = new JButton("Age/content limits");
		aside.add(btnAgecontentLimits, "cell 0 6");
		
		JButton btnCredits = new JButton("Credits");
		aside.add(btnCredits, "cell 0 7");
		
		JButton btnLogIn = new JButton("Log in");
		aside.add(btnLogIn, "cell 0 8");
		
		JButton btnExit = new JButton("Exit");
		aside.add(btnExit, "cell 0 9");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnLogIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
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
		btnConfigureExternalContent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ExternalMods().setVisible(true);
			}
		});
		btnDownloadContent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ModShop().setVisible(true);
			}
		});
		btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new SelectGame().setVisible(true);
			}
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainMenu.add(tabbedPane);
		
		JPanel panelSaves = new JPanel();
		tabbedPane.addTab("Saves", null, panelSaves, null);
		panelSaves.setLayout(new BorderLayout(0, 0));
		
		List list = new List();
		panelSaves.add(list, BorderLayout.CENTER);
		
		JPanel subPanelSaves = new JPanel();
		panelSaves.add(subPanelSaves, BorderLayout.SOUTH);
		subPanelSaves.setLayout(new MigLayout("", "[][][][]", "[]"));
		
		JButton btnPlay = new JButton("Play");
		subPanelSaves.add(btnPlay, "cell 0 0");
		
		JButton btnNewWorld = new JButton("New world");
		subPanelSaves.add(btnNewWorld, "cell 1 0");
		
		JButton btnReloadWorlds = new JButton("Refresh");
		subPanelSaves.add(btnReloadWorlds, "cell 2 0");
		
		JPanel panelMods = new JPanel();
		tabbedPane.addTab("Mods", null, panelMods, null);
		panelMods.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		
		JPanel subPanelMods = new JPanel();
		panelMods.add(subPanelMods, "cell 0 1,grow");
		subPanelMods.setLayout(new MigLayout("", "[]", "[]"));
		
		JLabel lblModCounter = new JLabel(GameContents.addons.size()+" mods");
		subPanelMods.add(lblModCounter, "cell 0 0");
		
		tablemodel.addColumn("Name");
		tablemodel.addColumn("Description");
		tablemodel.addColumn("State");
		tablemodel.addColumn("Last update");
		tablemodel.addColumn("Version");
		tablemodel.addColumn("Author");
		GameContents.addons.forEach((AddonInfo a) -> addMod(a));
		
		table = new JTable(tablemodel);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setViewportBorder(UIManager.getBorder("Button.border"));
		panelMods.add(scrollPane, "cell 0 0,grow");
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
	
	//XXX Mod table
	private final DefaultTableModel tablemodel = new DefaultTableModel();
	private JTable table;
	private void addMod(AddonInfo mod) {
		if(mod == null) return;
		String release = "Unknown", descr = "This file is corrupt.", author = "Unknown";
		if(mod.mmbmod == null) {
			release = new Date().toString();
			descr = "No description";
			author = "Unknown";
		}else if(mod.state == AddonState.ENABLE) {
			release = mod.mmbmod.release.toString();
			descr = mod.mmbmod.description;
			author = mod.mmbmod.author;
		}
		String state = mod.state.toString();
		
		String ver = "";
		String name = mod.name;
		tablemodel.addRow(new Object[] {name, descr, state, release, ver, author});
	}

	private void loginInfo() {
		//if(LoginInfo.loggedIn()) {
			
		//}
	}
}
