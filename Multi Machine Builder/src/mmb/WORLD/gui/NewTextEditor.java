/**
 * 
 */
package mmb.WORLD.gui;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import mmb.BEANS.TextMessageProvider;
import mmb.WORLD.block.BlockEntity;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class NewTextEditor extends JPanel {
	private static final long serialVersionUID = 109161698315300930L;
	
	private JTextPane textPane;
	private JButton btnOk;
	private JButton btnCancel;
	public final String title;
	private final WorldWindow frame;

	/**
	 * Create the panel.
	 */
	public NewTextEditor(TextMessageProvider sv, BlockEntity ent, WorldWindow frame) {
		this.frame = frame;
		title = "["+ent.posX()+","+ent.posY()+"]";
		setLayout(new MigLayout("", "[grow]", "[grow][21px][21px]"));
		
		textPane = new JTextPane();
		textPane.setText(sv.getMessage());
		add(textPane, "cell 0 0,grow");
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(e -> {
			sv.setMessage(textPane.getText());
			remove();
		});
		btnOk.setBackground(Color.GREEN);
		btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnOk, "cell 0 1,growx,aligny center");
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> remove());
		btnCancel.setBackground(Color.RED);
		btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnCancel, "cell 0 2,growx,aligny center");
	}
	private void remove() {
		frame.closeDialogWindow(this);
	}
}
