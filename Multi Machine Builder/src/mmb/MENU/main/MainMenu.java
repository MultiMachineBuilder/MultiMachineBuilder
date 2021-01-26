package mmb.MENU.main;

import java.awt.*;
import javax.swing.*;

import mmb.DATA.contents.GameContents;
import mmb.debug.Debugger;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import java.awt.Desktop;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import mmb.MENU.settings.PanelSettings;

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
	private JButton btnExit;
	private JLabel timerLBL;

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
		mm = this;
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
		aside.setLayout(new MigLayout("", "[]", "[][][][][]"));
		
		JButton btnWebsite = new JButton("Website");
		aside.add(btnWebsite, "cell 0 1");
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
		
		JButton btnLogIn = new JButton("Log in");
		aside.add(btnLogIn, "cell 0 2");
		btnLogIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		aside.add(btnExit, "cell 0 3");
		
		timerLBL = new JLabel("New label");
		aside.add(timerLBL, "cell 0 4");
		java.util.Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				GregorianCalendar date = new GregorianCalendar();
				int year = date.get(GregorianCalendar.YEAR);
				int month = date.get(GregorianCalendar.MONTH);
				int day = date.get(GregorianCalendar.DAY_OF_MONTH);
				int hour = date.get(GregorianCalendar.HOUR);
				int min = date.get(GregorianCalendar.MINUTE);
				int sec = date.get(GregorianCalendar.SECOND);
				
				StringBuilder message = new StringBuilder()
						.append(year)
						.append('/')
						.append(month)
						.append('/')
						.append(day)
						.append(' ');
				if(hour < 10) message.append('0');
				message.append(hour).append(':');
				if(min < 10) message.append('0');
				message.append(min).append(':');
				if(sec < 10) message.append('0');
				message.append(sec);
				timerLBL.setText(message.toString());
			}
		}, 0, 1000);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> timer.cancel()));
		
		/*ItemLabel itemLabel = new ItemLabel(new DrawerPlainColor(Color.BLUE), "test");
		aside.add(itemLabel, "cell 0 10");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});*/
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainMenu.add(tabbedPane);
		
		tabbedPane.addTab("Saves", null, new PanelSaves(), null);
		tabbedPane.addTab("Mods", null, new PanelMods(), null);
		tabbedPane.addTab("Settings", null, new PanelSettings(), null);
		tabbedPane.addTab("Shop", new PanelShop());
	}
	
	
	
	/**
	 * @return the fullScreen
	 */
	public boolean isFullScreen() {
		return FullScreen;
	}
	
	private void createAside() {
		
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
