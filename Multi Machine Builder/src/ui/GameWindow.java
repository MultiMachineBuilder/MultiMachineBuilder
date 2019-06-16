package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import addon.Addon;
import debug.Debugger;
import gamefiles.GameResource;

public class GameWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameWindow window = new GameWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Debugger.printl("Loading mods");
		Addon.initMods();
	}
	
	public void finalize() {
		//Shut down game
		GameResource.shutdown();
		Addon.shutdown();
		
	}

}
