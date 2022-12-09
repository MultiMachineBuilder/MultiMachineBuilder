/**
 * 
 */
package mmbbase.menu.world;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import mmb.content.wireworld.TextMessageProvider;
import mmb.engine.block.BlockEntity;
import mmbbase.menu.world.window.WorldWindow;

import javax.annotation.Nonnull;
import javax.swing.JButton;
import java.awt.Component;

import static mmb.engine.GlobalSettings.$res;

import java.awt.Color;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class TextEditor extends JPanel {
	private static final long serialVersionUID = 109161698315300930L;
	
	private JTextPane textPane;
	private JButton btnOk;
	private JButton btnCancel;
	@Nonnull public final String title;
	private final WorldWindow frame;

	/**
	 * Create the panel.
	 * @param sv block with editable text message
	 * @param ent block entity
	 * @param frame world window
	 */
	public TextEditor(TextMessageProvider sv, BlockEntity ent, WorldWindow frame) {
		this.frame = frame;
		title = "["+ent.posX()+","+ent.posY()+"]";
		setLayout(new MigLayout("", "[grow]", "[grow][21px][21px]"));
		
		textPane = new JTextPane();
		textPane.setText(sv.getMessage());
		add(textPane, "cell 0 0,grow");
		
		btnOk = new JButton($res("ok"));
		btnOk.addActionListener(e -> {
			sv.setMessage(textPane.getText());
			remove();
		});
		btnOk.setBackground(Color.GREEN);
		btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnOk, "cell 0 1,growx,aligny center");
		
		btnCancel = new JButton($res("cancel"));
		btnCancel.addActionListener(e -> remove());
		btnCancel.setBackground(Color.RED);
		btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnCancel, "cell 0 2,growx,aligny center");
	}
	private void remove() {
		frame.closeDialogWindow(this);
	}
}
