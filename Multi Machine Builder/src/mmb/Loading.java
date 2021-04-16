package mmb;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.DATA.Settings;
import mmb.MENU.main.MainMenu;
import mmb.MODS.loader.ModLoader;
import mmb.debug.Debugger;

import javax.swing.JLabel;
import java.io.File;
import net.miginfocom.swing.MigLayout;

public class Loading extends JFrame {
	private static final Debugger debug = new Debugger("LOAD");
	private static Loading loader;
	private final JPanel contentPane;
	JLabel st1 = new JLabel("State 1");
	JLabel st2 = new JLabel("State 2");
	JLabel st3 = new JLabel("State 3");
	JLabel st4 = new JLabel("State 4");

	public static void state1(String str) {
		loader.st1.setText(str);
	}
	public static void state2(String str) {
		loader.st2.setText(str);
	}
	public static void state3(String str) {
		loader.st3.setText(str);
	}
	public static void state4(String str) {
		loader.st4.setText(str);
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			String jversion = System.getProperty("java.version");
			boolean j8ol = jversion.charAt(0) == '1';
			debug.printl("Java version is: "+jversion.toString());
			if(!j8ol) {
				debug.printl("Changing scale because Java is >= 9");
				System.setProperty("sun.java2d.uiScale", "1.0");
				System.setProperty("prism.allowhidpi", "false");
				System.setProperty("sun.java2d.uiScale.enabled", "false");
				System.setProperty("sun.java2d.win.uiScaleX", "1.0");
				System.setProperty("sun.java2d.win.uiScaleY", "1.0");
			}
			
			//UI initialized here
			loader = new Loading();
			loader.setVisible(true);
			
			new Thread(() -> loader.continueLoading())
			.start();
		// deepcode ignore DontCatch: log the game crash		} catch (Throwable e) {
			Main.crash(e);
		}
	}

	/**
	 * Create the frame.
	 */
	public Loading() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[32px]", "[13px][13px][13px][13px]"));
		contentPane.add(st1, "cell 0 0,alignx center,aligny center");
		contentPane.add(st2, "cell 0 1,alignx center,aligny center");
		contentPane.add(st3, "cell 0 2,alignx center,aligny center");
		contentPane.add(st4, "cell 0 3,alignx center,aligny center");
		
	}
	
	void continueLoading() {
		Settings.loadSettings();
		String JGLLib = new File("./natives/").getAbsolutePath();
		System.setProperty("org.lwjgl.librarypath", JGLLib);
		ModLoader.modloading();
		MainMenu.create();
		setVisible(false); //you can't see me!
		dispose(); //Destroy the JFrame object
	}

}
