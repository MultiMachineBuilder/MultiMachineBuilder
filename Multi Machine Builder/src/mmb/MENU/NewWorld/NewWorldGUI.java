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
	private JPanel general;
	private JCheckBox chckbxCreative;
	private ButtonGroup worldtype = new ButtonGroup();
	private JLabel lblNewLabel;
	private GeneratorOptions generatorOptionsStructures;
	private GeneratorOptions generatorOptionsResources;
	private JList selWorldType;

	/**
	 * Create the panel.
	 */
	public NewWorldGUI() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		general = new JPanel();
		tabbedPane.addTab("General", null, general, null);
		general.setLayout(new MigLayout("", "[grow][grow]", "[][27.00][305.00][][][]"));
		
		chckbxCreative = new JCheckBox("Creative");
		general.add(chckbxCreative, "cell 0 0");
		
		lblNewLabel = new JLabel("World type:");
		general.add(lblNewLabel, "flowx,cell 0 1");
		
		generatorOptionsStructures = new GeneratorOptions("Structures");
		general.add(generatorOptionsStructures, "cell 0 2,grow");
		
		generatorOptionsResources = new GeneratorOptions("Resources");
		general.add(generatorOptionsResources, "cell 1 2,grow");
		
		selWorldType = new JList();
		general.add(selWorldType, "cell 0 1,grow");

	}

}
