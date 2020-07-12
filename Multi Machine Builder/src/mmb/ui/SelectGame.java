package mmb.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.JButton;

public class SelectGame extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SelectGame frame = new SelectGame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public SelectGame() {
		setTitle("Save manager");
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[94.00,grow][grow]"));
		
		table = new JTable();
		contentPane.add(table, "cell 0 0,grow");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("", "[][][][][][]", "[][][]"));
		
		JButton btnNew = new JButton("New");
		panel.add(btnNew, "cell 0 0");
		
		JButton btnTemplate = new JButton("Template");
		panel.add(btnTemplate, "cell 1 0");
		
		JButton btnUpload = new JButton("Upload");
		panel.add(btnUpload, "cell 4 0");
		
		JButton btnSettings = new JButton("Settings");
		panel.add(btnSettings, "cell 0 1");
		
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete, "cell 1 1");
		
		JButton btnPlay = new JButton("Play");
		panel.add(btnPlay, "cell 0 2");
	}

}
