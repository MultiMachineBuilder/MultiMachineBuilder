/**
 * 
 */
package mmb.menu.helper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import io.github.parubok.text.multiline.MultilineLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author oskar
 *
 */
public class BooleanDialog extends JDialog {
	private static final long serialVersionUID = -548563009384514938L;
	private boolean accepted = false;
	/**
	 * Creates a dialog with specific settings, asking for a boolean
	 * @param strTrue the text of true button
	 * @param strFalse the text of false button
	 * @param tooltip the window title
	 * @param message the string message
	 * @param action event listener to invoke
	 */
	public BooleanDialog(String strTrue, String strFalse, String tooltip, String message, BooleanConsumer action) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!accepted) action.accept(false);
			}
		});
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		setTitle(tooltip);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton(strTrue);
		okButton.addActionListener(e -> {
			action.accept(true);
			accepted = true;
			dispose();
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton(strFalse);
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> {
			action.accept(false);
			accepted = true;
			dispose();
		});
		buttonPane.add(cancelButton);
		MultilineLabel multilineLabel = new MultilineLabel();
		multilineLabel.setText(message);
		getContentPane().add(multilineLabel, BorderLayout.CENTER);
	}

}
