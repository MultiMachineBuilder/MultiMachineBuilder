package mmbbase.menu.main;

import static mmb.engine.settings.GlobalSettings.*;

import java.awt.*;

import javax.annotation.Nonnull;
import javax.swing.*;

import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

import mmb.engine.debug.Debugger;
import mmb.engine.gl.HalfVecTest;
import mmb.engine.mods.Mods;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.settings.Settings;
import mmb.engine.texture.Textures;
import mmb.engine.window.FullScreen;
import mmb.engine.window.MMBFrame;
import mmbbase.menu.components.BoundCheckBoxMenuItem;

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
	
	//Various webistes
	public static final String GITHUB = "https://github.com/MultiMachineBuilder/MultiMachineBuilder";
	private static final String helpUA = "https://good.od.ua/en/stopwar";
	//https://www.gov.pl/web/mswia-pl/informacja-dla-uchodżców-z-ukrainy
	private static final String refugeesEN = "https://www.gov.pl/web/mswia-en/information-for-refugees-from-ukraine";
	private static final String refugeesUA = "http://www.ua.gov.pl/";
	
	//Logos
	public static final BufferedImage GEAR = Textures.get("gearlogo.png");
	public static final BufferedImage DOLLAR = Textures.get("dollar.png");
	
	private JButton btnExit;
	private JLabel timerLBL;
	private JButton TEST;
	private JMenu mnUkraine;
	/**
	 * Launch the application.
	 */
	public static void create() {
		Settings.addSettingBool("fullscreen", false, FullScreen.isFullScreen);		
		FullScreen.setWindow(INSTANCE);
	}

	private MainMenu() {
		debug.printl("MainMenu created");
		setTitle("MultiMachineBuilder - "+Mods.mods.size()+" mods");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setIconImage(GEAR);
		
		mainMenuBar = new JMenuBar();
		setJMenuBar(mainMenuBar);
		
		JMenu mnNewMenu = new JMenu($res("cgui-win"));
		mainMenuBar.add(mnNewMenu);
		
		stngFullScreen = new BoundCheckBoxMenuItem("FullScreen");
		stngFullScreen.setVariable(FullScreen.isFullScreen);
		mnNewMenu.add(stngFullScreen);
		
		mnUkraine = new JMenu();
		mnUkraine.setBackground(new Color(30, 144, 255));
		mnUkraine.setIcon(new ImageIcon(Textures.get("ukraine.png")));
		mainMenuBar.add(mnUkraine);
		
		JMenuItem btnUkraine = new JMenuItem($res("cgui-refugeedonate"));
		mnUkraine.add(btnUkraine);
		Icon ua0 = new ImageIcon(Textures.get("ukraine.png"));
		btnUkraine.setIcon(ua0);
		btnUkraine.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(helpUA));
			} catch (Exception ex) {
				debug.pstm(ex, "Unable to help Ukrainian refugees");
			}
		});
		
		//This button is for English speakers. It leads to a site for refugees.
		JMenuItem btnRefugee = new JMenuItem($res("cgui-refugeehelp"));
		Icon ua1 = new ImageIcon(Textures.get("ukraine1.png"));
		btnRefugee.setIcon(ua1);
		mnUkraine.add(btnRefugee);
		btnRefugee.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(refugeesEN)); //An English-language site for Ukrainian refugees
			} catch (Exception ex) {
				debug.pstm(ex, "Unable to get help as a refugee");
			}
		});
		
		//This button is for Ukrainian speakers. It means the same thing as the English one
		JMenuItem btnRefugee1 = new JMenuItem(GlobalSettings.$res("ua-refugees"));
		btnRefugee1.setIcon(ua1);
		btnRefugee1.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(refugeesUA)); //An Ukrainian-language site for Ukrainian refugees
			} catch (Exception ex) {
				debug.pstm(ex, GlobalSettings.$res("ua-refugee-error"));
			}
		});
		mnUkraine.add(btnRefugee1);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel aside = new JPanel();
		contentPane.add(aside, BorderLayout.WEST);
		BoxLayout layout = new BoxLayout(aside, BoxLayout.Y_AXIS);
		aside.setLayout(layout);
		
		JButton btnWebsite = new JButton($res("cgui-website"));
		btnWebsite.setToolTipText("Open this game's website");
		aside.add(btnWebsite);
		btnWebsite.addActionListener(e ->{
			try {
				Desktop.getDesktop().browse(new URI(GITHUB));
			} catch (Exception ex) {
				debug.pstm(ex, "Unable to open GitHub");
			}
		});
		btnExit = new JButton($res("cgui-exit"));
		btnExit.setBackground(Color.RED);
		btnExit.setToolTipText("Exit the game");
		btnExit.addActionListener(e -> System.exit(0));
		aside.add(btnExit);
		
		TEST = new JButton("TEST");
		TEST.addActionListener(e -> {
			Thread thread = new Thread(HalfVecTest::run);
			thread.start();
		});
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
		tabbedPane.addTab($res("cgui-saves"), null, PanelSaves.INSTANCE, null);
		tabbedPane.addTab($res("cgui-mods"), null, PanelMods.INSTANCE, null);
		tabbedPane.addTab($res("cgui-settings"), null, new PanelSettings(), null);
		tabbedPane.addTab($res("cgui-shop"), new PanelShop());
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
