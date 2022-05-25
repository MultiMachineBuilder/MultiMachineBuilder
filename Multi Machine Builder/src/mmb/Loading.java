package mmb;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.DATA.Settings;
import mmb.MENU.main.MainMenu;
import mmb.MODS.loader.ModLoader;
import mmb.debug.Debugger;

import javax.swing.JLabel;
import java.io.File;
import java.util.Locale;

import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 * A dialog window with loading information
 */
public class Loading extends JFrame {
	private static final long serialVersionUID = -8763636851592865062L;
	private static final Debugger debug = new Debugger("LOADING FRAME");
	
	private final JPanel contentPane;
	JLabel st1 = new JLabel("State 1");
	JLabel st2 = new JLabel("State 2");

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
	}
	
	void continueLoading() {
		try {
			Settings.loadSettings();
			GlobalSettings.init();
			String lwjgl = new File("./natives/").getAbsolutePath();
			System.setProperty("org.lwjgl.librarypath", lwjgl);
			ModLoader.modloading(); //the main loading method
			Locale locale = GlobalSettings.locale();
			
			//localized welcome
			debug.printl(GlobalSettings.$res("hello"));
			
			JComponent.setDefaultLocale(locale);
			Locale.setDefault(locale);
			MainMenu.create();
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}catch(Throwable e) {
			debug.printerrl("FATAL ERROR WHILE LOADING");
			Main.crash(e);
		}
		
	}

}
