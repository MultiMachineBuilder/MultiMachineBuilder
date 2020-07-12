package mmb.ui.editor;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class LabelEditor extends JPanel {

	/**
	 * Create the panel.
	 */
	public LabelEditor() {
		setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JToolBar toolBar = new JToolBar();
		add(toolBar, "cell 0 0");
		
		JEditorPane editorPane = new JEditorPane();
		add(editorPane, "cell 0 1,grow");

	}

}
