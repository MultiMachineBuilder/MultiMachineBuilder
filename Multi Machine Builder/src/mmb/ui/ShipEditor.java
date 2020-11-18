package mmb.ui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.List;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class ShipEditor extends JPanel {
	private final JTabbedPane main;
	private final JPanel toolbar;
	private final JToolBar toolbarInner;
	private final JPanel hull;
	private final JPanel panel_1;
	/**
	 * Create the panel.
	 */
	public ShipEditor() {
		setBounds(0, 0, 640, 480);
		setLayout(new BorderLayout(0, 0));
		
		main = new JTabbedPane(JTabbedPane.TOP);
		main.setToolTipText("");
		add(main, BorderLayout.CENTER);
		
		hull = new JPanel();
		main.addTab("Hull editor", null, hull, null);
		
		panel_1 = new JPanel();
		main.addTab("New tab", null, panel_1, null);
		
		toolbar = new JPanel();
		add(toolbar, BorderLayout.EAST);
		toolbar.setLayout(new MigLayout("", "[]", "[][]"));
		
		toolbarInner = new JToolBar();
		toolbarInner.setOrientation(SwingConstants.VERTICAL);
		toolbar.add(toolbarInner, "cell 0 0");

	}
}
