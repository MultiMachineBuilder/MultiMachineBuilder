package ui;

import javax.swing.JPanel;

import addon.Addon;

import javax.swing.JLabel;
import javax.swing.JButton;

public class AddonUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2845674310726934843L;
	JLabel lblSomeAddon = new JLabel("Some addon...");
	JLabel lblThisAddonIs = new JLabel("This addon is");
	JLabel lblAvaliability = new JLabel("avaliable");
	JButton btnMoreInformation = new JButton("More information");
	JButton btnReportBug = new JButton("Report bug");
	/**
	 * Create the panel.
	 */
	public AddonUI(Addon addon) {
		setLayout(null);
		
		lblSomeAddon.setBounds(10, 11, 430, 14);
		add(lblSomeAddon);
		
		lblThisAddonIs.setBounds(10, 36, 68, 14);
		add(lblThisAddonIs);
		
		lblAvaliability.setBounds(76, 36, 58, 14);
		add(lblAvaliability);
		
		btnMoreInformation.setBounds(10, 61, 113, 23);
		add(btnMoreInformation);
		
		btnReportBug.setBounds(133, 61, 89, 23);
		add(btnReportBug);
		
		

	}
}
