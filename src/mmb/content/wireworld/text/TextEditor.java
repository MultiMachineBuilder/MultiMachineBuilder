/**
 * 
 */
package mmb.content.wireworld.text;

import javax.swing.JTextPane;

import mmb.engine.block.BlockEntity;
import mmb.menu.world.window.*;

import javax.swing.JButton;
import java.awt.Component;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import net.miginfocom.swing.MigLayout;

/**
 * Edits blocks with text message support.
 * Click OK to save changes, or click Cancel to discard changes
 * @author oskar
 * @see TextMessageProvider
 * @see TextChatter
 */
public class TextEditor extends GUITab {
	private static final long serialVersionUID = 109161698315300930L;
	
	private JTextPane textPane;
	private JButton btnOk;
	private JButton btnCancel;
	public final String title;
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
			frame.closeWindow(this);
		});
		btnOk.setBackground(Color.GREEN);
		btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnOk, "cell 0 1,growx,aligny center");
		
		btnCancel = new JButton($res("cancel"));
		btnCancel.addActionListener(e -> frame.closeWindow(this));
		btnCancel.setBackground(Color.RED);
		btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnCancel, "cell 0 2,growx,aligny center");
	}

	@Override
	public void close(WorldWindow window) {
		//unused
	}
}
