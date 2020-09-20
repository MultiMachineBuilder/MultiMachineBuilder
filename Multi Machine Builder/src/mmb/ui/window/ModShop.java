package mmb.ui.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JList;
import net.miginfocom.swing.MigLayout;

public class ModShop extends JFrame {
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ModShop() {
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[238px,grow][174px][grow]", "[245px,grow]"));
		
		JPanel settings = new JPanel();
		contentPane.add(settings, "cell 0 0,grow");
		settings.setLayout(new MigLayout("", "[]", "[]"));
		
		JButton btnDownload = new JButton("Download");
		settings.add(btnDownload, "cell 0 0");
		
		JList list = new JList();
		
		contentPane.add(list, "cell 1 0,grow");
		
		JPanel info = new JPanel();
		contentPane.add(info, "cell 2 0,grow");
		info.setLayout(new MigLayout("", "[46px]", "[14px][]"));
		
		JLabel modSize = new JLabel("Download size");
		info.add(modSize, "cell 0 0,alignx left,aligny top");
		
		JLabel lblAgeRating = new JLabel("Age rating");
		info.add(lblAgeRating, "cell 0 1");
	}
	
	private void addData() {
		//Add downloadable mods
	}

}
