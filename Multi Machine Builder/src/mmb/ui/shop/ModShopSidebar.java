package mmb.ui.shop;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import net.miginfocom.swing.*;

public class ModShopSidebar extends JPanel {

	/**
	 * Create the panel.
	 */
	public ModShopSidebar() {
		setLayout(new MigLayout("", "[65px][61px][40px][29px][101px][113px]", "[23px][][][][]"));
		
		JButton btnReload = new JButton("Reload");
		add(btnReload, "cell 0 0 2 1,alignx left,aligny top");
		
		JButton btnLogIn = new JButton("Log in");
		add(btnLogIn, "cell 0 1 2 1,alignx left,aligny top");
		
		JLabel lblAgeLimit = new JLabel("Age limit");
		add(lblAgeLimit, "cell 0 2,alignx left,aligny center");
		
		JSpinner spinner = new JSpinner();
		add(spinner, "cell 1 2,alignx left,aligny center");
		
		JButton btnBuydownload = new JButton("Buy/download");
		add(btnBuydownload, "cell 0 3 2 1,alignx left,aligny top");
		
		JButton btnMoreInformation = new JButton("More information");
		add(btnMoreInformation, "cell 0 4 2 1,alignx left,aligny top");

	}

}
