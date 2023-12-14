package mmb;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.engine.MMBUtils;
import mmb.engine.debug.Debugger;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.settings.Settings;
import mmb.menu.main.MainMenu;

import javax.swing.JLabel;

import java.awt.EventQueue;
import java.io.File;
import java.util.function.Consumer;

import net.miginfocom.swing.MigLayout;
import ru.krlvm.swingdpi.SwingDPI;

/**
 * The entry point to the game, and a dialog window with loading information
 * @author oskar
 */
public class Main extends JFrame {
	private static final long serialVersionUID = -8763636851592865062L;
	private static final Debugger debug = new Debugger("MAIN");
	
	private final JPanel contentPane;

	/** Create the frame. */
	public Main() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[32px]", "[13px][13px][13px][13px]"));
		contentPane.add(st1, "cell 0 0,alignx center,aligny center");
		contentPane.add(st2, "cell 0 1,alignx center,aligny center");		
	}

	//Running check
	private static boolean running;
	/**
	 * Checks if the game is run as executable. Used to suppress debugger initialization when designing GUIs
	 * @return is the game executed?
	 */
	public static boolean isRunning() {
		return Main.running;
	}

	//Crash handling
	/**
	 * Crashes the game to the desktop
	 * @param e throwable, which caused the crash
	 */
	public static void crash(Throwable e) {
		debug.stacktraceError(e, "GAME HAS CRASHED");
		if(errorHook != null) {
			 errorHook.accept(e);
			 MMBUtils.shoot(e);
		}
		else System.exit(1);
	}
	@Nil private static Consumer<Throwable> errorHook;
	/**
	 * @discouraged DO NOT USE THIS METHOD EXCEPT IN TESTS
	 * @param eh error hook
	 */
	public static void errorhook(Consumer<Throwable> eh) {
		if(running && errorHook != null) throw new IllegalStateException("Debugging only");
		errorHook = eh;
	}
	
	//Labels
	private JLabel st1 = new JLabel("State 1");
	private JLabel st2 = new JLabel("State 2");
	/**
	 * Sets the primary stste
	 * @param str the state string
	 */
	public static void state1(String str) {
		loader.st1.setText(str);
		debug.printl("State: "+str);
	}
	/**
	 * Sets the secondary state
	 * @param str the state string
	 */
	public static void state2(String str) {
		loader.st2.setText(str);
	}

	//Main method
	private static Main loader;
	/**
	 * Runs the game
	 * @param args the command line arguments
	 */
	public static void main(String... args) {
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
			GlobalSettings.init();
			Settings.loadSettings();
			GlobalSettings.translate();
			
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
			EventQueue.invokeLater(() -> {
				try {
					//LWJGL
					String lwjgl = new File("./natives/").getAbsolutePath();
					System.setProperty("org.lwjgl.librarypath", lwjgl);
					
					GameLoader.modloading(); //the main loading method
					
					//localized welcome
					debug.printl(GlobalSettings.$res("hello"));
					
					//create main menu
					MainMenu.create();
					loader.setVisible(false); //you can't see me!
					loader.dispose(); //Destroy the JFrame object
				}catch(Throwable e) { //NOSONAR log the game crash
					debug.printerrl("FATAL ERROR WHILE LOADING");
					Main.crash(e);
				}
			});
		} catch (Throwable e) { //NOSONAR log the game crash
			Main.crash(e);
		}
	}
	
	/**
	 * Called when an exception is left uncaught in any thread
	 * @param thread thread, where exception was thrown
	 * @param ex the exception itself
	 */
	public static void uncaughtException(Thread thread, Throwable ex) {
		Debugger debug1 = new Debugger(thread.getName());
		debug1.stacktraceError(ex, "A thread has thrown an exception");
	}
}
