package mmb.ui.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.vfs2.FileSystemException;

import mmb.addon.loader.ModLoader;
import mmb.debug.Debugger;

import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

public class Loading extends JFrame {
	private static Debugger debug = new Debugger("LOAD");
	static public Loading loader;
	private JPanel contentPane;
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
					loader.continueLoading();
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
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		GridBagConstraints gbc_st1 = new GridBagConstraints();
		gbc_st1.insets = new Insets(0, 0, 5, 0);
		gbc_st1.gridx = 0;
		gbc_st1.gridy = 3;
		contentPane.add(st1, gbc_st1);
		
		GridBagConstraints gbc_st2 = new GridBagConstraints();
		gbc_st2.insets = new Insets(0, 0, 5, 0);
		gbc_st2.gridx = 0;
		gbc_st2.gridy = 4;
		contentPane.add(st2, gbc_st2);
		
		GridBagConstraints gbc_st3 = new GridBagConstraints();
		gbc_st3.insets = new Insets(0, 0, 5, 0);
		gbc_st3.gridx = 0;
		gbc_st3.gridy = 5;
		contentPane.add(st3, gbc_st3);
		
		GridBagConstraints gbc_st4 = new GridBagConstraints();
		gbc_st4.gridx = 0;
		gbc_st4.gridy = 6;
		contentPane.add(st4, gbc_st4);
		
	}
	
	void continueLoading() {
		String JGLLib = new File("./natives/").getAbsolutePath();
		System.setProperty("org.lwjgl.librarypath", JGLLib);
		try {
			ModLoader.modloading();
		} catch (FileSystemException e) {
			debug.pstm(e, "Unable to load mods");
		}
		MainMenu.running();
		setVisible(false); //you can't see me!
		dispose(); //Destroy the JFrame object
	}
	private static void createRandomErrors() throws IllegalAccessException, FileNotFoundException, IOException, MalformedURLException {
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
