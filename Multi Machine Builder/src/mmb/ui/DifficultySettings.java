/**
 * 
 */
package mmb.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author oskar
 *
 */
public class DifficultySettings extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtScienceIncome;
	private JTextField txtMoneyIncome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DifficultySettings dialog = new DifficultySettings();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DifficultySettings() {
		setTitle("Difficulty settings");
		setBounds(100, 100, 450, 785);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtScienceIncome = new JTextField();
		txtScienceIncome.setToolTipText("Science income");
		txtScienceIncome.setBounds(338, 11, 86, 20);
		contentPanel.add(txtScienceIncome);
		txtScienceIncome.setColumns(10);
		
		JLabel lblScienceIncome = new JLabel("Science income");
		lblScienceIncome.setBounds(242, 11, 86, 20);
		contentPanel.add(lblScienceIncome);
		
		txtMoneyIncome = new JTextField();
		txtMoneyIncome.setToolTipText("Money income mutiplier");
		txtMoneyIncome.setBounds(338, 42, 86, 20);
		contentPanel.add(txtMoneyIncome);
		txtMoneyIncome.setColumns(10);
		
		JLabel lblMonetaryIncome = new JLabel("Monetary income");
		lblMonetaryIncome.setBounds(242, 42, 86, 20);
		contentPanel.add(lblMonetaryIncome);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
