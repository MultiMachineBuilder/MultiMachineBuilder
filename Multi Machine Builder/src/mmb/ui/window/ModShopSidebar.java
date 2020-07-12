package mmb.ui.window;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class ModShopSidebar extends JPanel {

	/**
	 * Create the panel.
	 */
	public ModShopSidebar() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JButton btnReload = new JButton("Reload");
		add(btnReload, "2, 2");
		
		JButton btnLogIn = new JButton("Log in");
		add(btnLogIn, "2, 4");
		
		JLabel lblAgeLimit = new JLabel("Age limit");
		add(lblAgeLimit, "2, 6");
		
		JSpinner spinner = new JSpinner();
		add(spinner, "4, 6");
		
		JButton btnBuydownload = new JButton("Buy/download");
		add(btnBuydownload, "2, 8");
		
		JButton btnMoreInformation = new JButton("More information");
		add(btnMoreInformation, "2, 10");

	}

}
