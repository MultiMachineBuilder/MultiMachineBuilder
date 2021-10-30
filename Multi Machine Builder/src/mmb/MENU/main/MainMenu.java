package mmb.MENU.main;

import java.awt.*;

import javax.annotation.Nonnull;
import javax.swing.*;

import mmb.DATA.Settings;
import mmb.DATA.contents.GameContents;
import mmb.debug.Debugger;
import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import java.awt.Desktop;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import mmb.MENU.FullScreen;
import mmb.MENU.MMBFrame;
import mmb.MENU.TestCollision;
import mmb.MENU.components.BoundCheckBoxMenuItem;
import mmb.MENU.settings.PanelSettings;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 * A singleton main menu class
 */
public class MainMenu extends MMBFrame {
	private static final long serialVersionUID = -7953512837841781519L;
	
	//Debugging
	private static final Debugger debug = new Debugger("Main menu");
	
	//ToolKit API
	/** The singleton instance of main menu*/
	@Nonnull public static final MainMenu INSTANCE = new MainMenu();
	/**
	 * Add a main menu tab
	 * @param tab tab contents
	 * @param name tab name
	 */
	public void addTab(Component tab, String name) {
		tabbedPane.add(name, tab);
	}
	/**
	 * Add a main menu toolbar entry
	 * @param menu {@link JMenu} to add
	 */
	public void addToolBarEntry(JMenu menu) {
		mainMenuBar.add(menu);
	}
	protected JMenuBar mainMenuBar;
	protected JTabbedPane tabbedPane;
	BoundCheckBoxMenuItem stngFullScreen;
	
	private final JPanel contentPane;
	public static final String GITHUB = "https://github.com/MultiMachineBuilder/MultiMachineBuilder";
	
	private JButton btnExit;
	private JLabel timerLBL;
	private JButton TEST;
	/**
	 * Launch the application.
	 */
	public static void create() {
		FullScreen.isFullScreen.setValue(Settings.getBool("fullscreen", false));
		FullScreen.setWindow(INSTANCE);
	}

	/**
	 * Create the frame.
	 */
	private MainMenu() {
		debug.printl("MainMenu created");
		setTitle("MultiMachineBuilder - "+GameContents.addons.size()+" mods");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		mainMenuBar = new JMenuBar();
		setJMenuBar(mainMenuBar);
		
		JMenu mnNewMenu = new JMenu("Window");
		mainMenuBar.add(mnNewMenu);
		
		stngFullScreen = new BoundCheckBoxMenuItem("FullScreen");
		stngFullScreen.setVariable(FullScreen.isFullScreen);
		mnNewMenu.add(stngFullScreen);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel aside = new JPanel();
		contentPane.add(aside, BorderLayout.WEST);
		BoxLayout layout = new BoxLayout(aside, BoxLayout.Y_AXIS);
		aside.setLayout(layout);
		
		JButton btnWebsite = new JButton("Website");
		btnWebsite.setToolTipText("Open this game's website");
		aside.add(btnWebsite);
		btnWebsite.addActionListener(e ->{
			try {
				Desktop.getDesktop().browse(new URI(GITHUB));
			} catch (Exception ex) {
				debug.pstm(ex, "Unable to open GitHub");
			}
		});
		btnExit = new JButton("Exit");
		btnExit.setToolTipText("Exit the game");
		btnExit.addActionListener(e -> System.exit(0));
		aside.add(btnExit);
		
		TEST = new JButton("TEST");
		TEST.addActionListener(e -> new TestCollision().setVisible(true));
		aside.add(TEST);
		
		timerLBL = new JLabel("Current time goes here");
		timerLBL.setToolTipText("The current time");
		aside.add(timerLBL);
		java.util.Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				refreshTime();
			}
		}, 0, 1000);
		Runtime.getRuntime().addShutdownHook(new Thread(timer::cancel));

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab("Saves", null, PanelSaves.INSTANCE, null);
		tabbedPane.addTab("Mods", null, new PanelMods(), null);
		tabbedPane.addTab("Settings", null, new PanelSettings(), null);
		tabbedPane.addTab("Shop", new PanelShop());
	}
	
	private void refreshTime() {
		GregorianCalendar date = new GregorianCalendar();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR);
		int min = date.get(Calendar.MINUTE);
		int sec = date.get(Calendar.SECOND);
		
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
	@Override
	public void destroy() {
		//unused
	}
}
