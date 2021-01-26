package mmb;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.MENU.main.MainMenu;
import mmb.MODS.loader.ModLoader;
import mmb.debug.Debugger;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import net.miginfocom.swing.MigLayout;

public class Loading extends JFrame {
	private static final Debugger debug = new Debugger("LOAD");
	static public Loading loader;
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Date date = new Date();
					boolean test = date.getHours() < 6 || date.getHours() >= 21;
							test = false;
					if(test) {
						createRandomErrors();
					}
					loader = new Loading();
					loader.setVisible(true);
					new Thread(() -> {
						loader.continueLoading();
					}).start();
				} catch (Exception e) {
					debug.pstm(e, "GAME HAS CRASHED");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Loading() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		String JGLLib = new File("./natives/").getAbsolutePath();
		System.setProperty("org.lwjgl.librarypath", JGLLib);
		ModLoader.modloading();
		MainMenu.running();
		setVisible(false); //you can't see me!
		dispose(); //Destroy the JFrame object
	}
	private static void createRandomErrors() throws IllegalAccessException, IOException {
		int x = (int) (Math.random() * 6);
		switch(x) {
		case 0:
			throw new NullPointerException();
		case 1:
			throw new ArrayIndexOutOfBoundsException();
		case 2:
			throw new IllegalAccessException();
		case 3:
			throw new FileNotFoundException();
		case 4:
			throw new IOException();
		case 5:
			throw new MalformedURLException();
		}
	}

}
