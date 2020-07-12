package mmb.ui.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.Box;
import net.miginfocom.swing.MigLayout;

public class NSFWLimits extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 *//*
	public static void main(String[] args) {
		try {
			NSFWLimits dialog = new NSFWLimits();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public NSFWLimits() {
		setTitle("Age & Content Limits");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new MigLayout("", "[43px][65px][69px][53px][49px][29px][40px]", "[23px][][][][][]"));

			JCheckBox chckbxSex = new JCheckBox("Sex");
			contentPanel.add(chckbxSex, "cell 0 0 2 1,alignx left,aligny top");
			
				JCheckBox chckbxViolence = new JCheckBox("Violence");
				contentPanel.add(chckbxViolence, "cell 0 1 2 1,alignx left,aligny top");
				
					JCheckBox chckbxProfanity = new JCheckBox("Profanity");
					contentPanel.add(chckbxProfanity, "cell 0 2 2 1,alignx left,aligny top");
					
						JCheckBox chckbxDrugs = new JCheckBox("Drugs");
						contentPanel.add(chckbxDrugs, "cell 0 3 2 1,alignx left,aligny top");
						
							JCheckBox chckbxGore = new JCheckBox("Gore");
							contentPanel.add(chckbxGore, "cell 0 4 2 1,alignx left,aligny top");
							
								JLabel lblAgeLimit = new JLabel("Age limit");
								contentPanel.add(lblAgeLimit, "cell 0 5,alignx left,aligny center");
								
									JSpinner spinner = new JSpinner();
									contentPanel.add(spinner, "cell 1 5,alignx left,aligny center");
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
