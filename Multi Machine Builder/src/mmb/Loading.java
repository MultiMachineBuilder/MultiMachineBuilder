package mmb;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.DATA.Settings;
import mmb.MENU.main.MainMenu;
import mmb.MODS.loader.ModLoader;
import javax.swing.JLabel;
import java.io.File;
import net.miginfocom.swing.MigLayout;

public class Loading extends JFrame {
	private static final long serialVersionUID = -8763636851592865062L;
	
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
		Settings.loadSettings();
		String lwjgl = new File("./natives/").getAbsolutePath();
		System.setProperty("org.lwjgl.librarypath", lwjgl);
		ModLoader.modloading();
		MainMenu.create();
		setVisible(false); //you can't see me!
		dispose(); //Destroy the JFrame object
	}

}
