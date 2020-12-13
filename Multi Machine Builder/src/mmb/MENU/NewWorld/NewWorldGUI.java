/**
 * 
 */
package mmb.MENU.NewWorld;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JList;
import mmb.MENU.CheckBoxList;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JScrollBar;

/**
 * @author oskar
 *
 */
public class NewWorldGUI extends JPanel {
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JCheckBox chckbxCreative;
	private ButtonGroup worldtype = new ButtonGroup();
	private JLabel lblNewLabel;
	private GeneratorOptions generatorOptionsStructures;
	private GeneratorOptions generatorOptionsResources;

	/**
	 * Create the panel.
	 */
	public NewWorldGUI() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		tabbedPane.addTab("General", null, panel, null);
		panel.setLayout(new MigLayout("", "[grow][grow]", "[][27.00][305.00][][][]"));
		
		chckbxCreative = new JCheckBox("Creative");
		panel.add(chckbxCreative, "cell 0 0");
		
		lblNewLabel = new JLabel("World type:");
		panel.add(lblNewLabel, "cell 0 1");
		
		generatorOptionsStructures = new GeneratorOptions("Structures");
		panel.add(generatorOptionsStructures, "cell 0 2,grow");
		
		generatorOptionsResources = new GeneratorOptions("Resources");
		panel.add(generatorOptionsResources, "cell 1 2,grow");

	}

}
