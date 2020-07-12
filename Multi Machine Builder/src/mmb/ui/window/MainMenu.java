package mmb.ui.window;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import mmb.debug.Debugger;
import mmb.ui.ExternalMods;
import mmb.ui.Flight;
import mmb.ui.SelectGame;

import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

public class MainMenu extends JFrame {
	private Debugger debug = new Debugger("Main menu");
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void running() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setTitle("MultiMachineBuilder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[119px][103px][105px]", "[23px][23px][23px][]"));
		
		JButton btnNewGame = new JButton("Play");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//new SelectGame().setVisible(true);
				Flight f = new Flight();
				f.setVisible(true);
				try {
				    TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException ie) {
					debug.pst(ie);
				    Thread.currentThread().interrupt();
				}
				f.start();
				
			}
		});
		contentPane.add(btnNewGame, "cell 0 0 3 1,growx,aligny top");
		
		JButton btnSettings = new JButton("Settings");
		contentPane.add(btnSettings, "cell 0 1,growx,aligny top");
		
		JButton btnMods = new JButton("Mods");
		btnMods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ModList();
			}
		});
		contentPane.add(btnMods, "cell 1 1,growx,aligny top");
		
		JButton btnCredits = new JButton("Credits");
		contentPane.add(btnCredits, "cell 2 1,growx,aligny top");
		
		JButton btnDownloadContent = new JButton("Download content");
		btnDownloadContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ModShop().setVisible(true);
			}
		});
		contentPane.add(btnDownloadContent, "cell 0 2,growx,aligny top");
		
		JButton btnWebsite = new JButton("Website (WIP)");
		contentPane.add(btnWebsite, "cell 1 2,growx,aligny top");
		
		JButton btnLogIn = new JButton("Log in");
		contentPane.add(btnLogIn, "cell 2 2,growx,aligny top");
		
		JButton btnConfigureExternalContent = new JButton("Configure external content");
		btnConfigureExternalContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExternalMods().setVisible(true);
			}
		});
		contentPane.add(btnConfigureExternalContent, "cell 0 3");
		
		JButton btnAgecontentLimits = new JButton("Age/content limits");
		contentPane.add(btnAgecontentLimits, "cell 1 3");
	}
}
