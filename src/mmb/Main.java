package mmb;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.data.Settings;
import mmb.debug.Debugger;
import mmb.gl.HalfVecTest;
import mmb.menu.main.MainMenu;
import mmb.mods.loader.ModLoader;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.util.Locale;

import net.miginfocom.swing.MigLayout;
import ru.krlvm.swingdpi.SwingDPI;

/**
 * @author oskar
 * A dialog window with loading information
 */
public class Main extends JFrame {
	private static final long serialVersionUID = -8763636851592865062L;
	private static final Debugger debug = new Debugger("MAIN");
	
	private final JPanel contentPane;
	private JLabel st1 = new JLabel("State 1");
	private JLabel st2 = new JLabel("State 2");
	private final boolean shouldWork;
	private static Main loader;

	/**
	 * Create the frame.
	 */
	public Main() {
		String jversion = System.getProperty("java.version");
		shouldWork = !jversion.startsWith("1.");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[32px]", "[13px][13px][13px][13px]"));
		contentPane.add(st1, "cell 0 0,alignx center,aligny center");
		if(!shouldWork) {
			Font font = new Font("Tahoma", Font.PLAIN, 20);
			st1.setFont(font);
			st1.setForeground(Color.RED);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			String a = "The Java version you have is "+jversion.charAt(2)+". The game won't work.";
			st1.setText(a);
			return;
		}
		contentPane.add(st2, "cell 0 1,alignx center,aligny center");		
	}

	private static boolean running;
	/**
	 * Checks if the game is run as executable. Used to suppress debugger initialization when designing GUIs
	 * @return is the game executed?
	 */
	public static boolean isRunning() {
		return Main.running;
	}

	/**
	 * Crashes the game to the desktop
	 * @param e throwable, which caused the crash
	 * @throws SecurityException if run by the mod
	 */
	public static void crash(Throwable e) {
		debug.pstm(e, "GAME HAS CRASHED");
		System.exit(1);
	}
	public static void state1(String str) {
		loader.st1.setText(str);
		debug.printl("State: "+str);
	}
	public static void state2(String str) {
		loader.st2.setText(str);
	}

	/**
	 * Runs the game
	 * @param args the comand line arguments
	 */
	public static void main(String[] args) {
		if(running) throw new IllegalStateException("The game is already running");
		running = true;
		
		//init debugger
		Debugger.init();
		
		//count RAM
		debug.printl("RAM avaliable: "+Runtime.getRuntime().maxMemory());
		Thread.setDefaultUncaughtExceptionHandler(Main::uncaughtException);
		try {
			String jversion = System.getProperty("java.version");
			debug.printl("Java version is: "+jversion);
			
			//Settings
			Settings.loadSettings();
			GlobalSettings.init();
			
			//Scaling
			double scale = GlobalSettings.uiScale.getDouble();
			if(scale < 0.01) {
				GlobalSettings.uiScale.set(1);
				scale = 1;
			}
			debug.printl("Scale: "+scale);
			boolean isSDPI = GlobalSettings.sysscale.getValue();
			if(isSDPI) SwingDPI.disableJava9NativeScaling();
			SwingDPI.setScaleFactor((float) scale);
			
			//UI initialized here
			loader = new Main();
			loader.setVisible(true);
			if(Main.loader.shouldWork) EventQueue.invokeLater(() -> {
				try {
					//LWJGL
					String lwjgl = new File("./natives/").getAbsolutePath();
					System.setProperty("org.lwjgl.librarypath", lwjgl);
					
					ModLoader.modloading(); //the main loading method
					
					//localized welcome
					Locale locale = GlobalSettings.locale();
					debug.printl(GlobalSettings.$res("hello"));
					JComponent.setDefaultLocale(locale);
					Locale.setDefault(locale);
					
					final boolean testingShunt = false;
					if(testingShunt) {
						HalfVecTest.run();
						return;
					}
					
					//create main menu
					MainMenu.create();
					loader.setVisible(false); //you can't see me!
					loader.dispose(); //Destroy the JFrame object
				}catch(Throwable e) {
					debug.printerrl("FATAL ERROR WHILE LOADING");
					Main.crash(e);
				}
			});
		} catch (Throwable e) { //NOSONAR log the game crash
			Main.crash(e);
		}
	}
	
	public static void uncaughtException(@SuppressWarnings("null") Thread thread, @SuppressWarnings("null") Throwable ex) {
		@SuppressWarnings("null")
		Debugger debug = new Debugger(thread.getName());
		debug.pstm(ex, "A thread has thrown an exception");
	}
}
