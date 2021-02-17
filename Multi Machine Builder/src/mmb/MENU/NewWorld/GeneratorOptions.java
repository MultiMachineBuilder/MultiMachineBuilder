/**
 * 
 */
package mmb.MENU.NewWorld;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollBar;
import javax.swing.ListModel;

import mmb.MENU.components.CheckBoxList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 *
 */
public class GeneratorOptions extends JPanel {
	private JLabel lblName;
	private JButton btnDeleteAll;
	private JButton btnSelectAll;
	private String title;
	private CheckBoxList checkBoxList;
	private JLabel lblFewer;
	private JScrollBar scrollBar;
	private JLabel lblMore;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
		lblName.setText(title+":");
		lblFewer.setText("Fewer "+title);
		lblName.setText("More "+title);
	}
	/**
	 * Create the panel.
	 * @wbp.parser.constructor
	 */
	public GeneratorOptions(String title) {
		this.title = title;
		init(new String[0]);
	}
	public GeneratorOptions(String[] options, String title) {
		this.title = title;
		init(options);
	}
	public GeneratorOptions(Collection<String> options, String title) {
		this(options.toArray(new String[options.size()]), title);
	}
	private void init(String[] options) {
		setLayout(new MigLayout("", "[grow]", "[grow][][]"));
		
		lblName = new JLabel(title+":");
		add(lblName, "flowx,cell 0 0");
		
		btnSelectAll = new JButton("All");
		btnSelectAll.addActionListener(e -> {
				for(JCheckBox chckbx: getChecks()) {
					chckbx.setSelected(true);
				}
		});
		add(btnSelectAll, "flowx,cell 0 1,growx");
		
		btnDeleteAll = new JButton("None");
		btnDeleteAll.addActionListener(e -> {
			for(JCheckBox chckbx: getChecks()) 
				chckbx.setSelected(false);
		});
		add(btnDeleteAll, "cell 0 1,growx");
		
		checkBoxList = new CheckBoxList();
		add(checkBoxList, "cell 0 0,grow");
		
		lblFewer = new JLabel("Fewer "+title);
		add(lblFewer, "flowx,cell 0 2");
		
		scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		add(scrollBar, "cell 0 2,growx");
		
		lblMore = new JLabel("More "+title);
		add(lblMore, "cell 0 2");
	}
	
	public boolean[] getSelections() {
		@SuppressWarnings("unchecked")
		ListModel<JCheckBox> model = checkBoxList.getModel();
		boolean[] result = new boolean[model.getSize()];
		for(int i = 0; i < result.length; i++) {
			result[i] = model.getElementAt(i).isSelected();
		}
		return result;
	}
	public JCheckBox[] getChecks() {
		@SuppressWarnings("unchecked")
		ListModel<JCheckBox> model = checkBoxList.getModel();
		JCheckBox[] result = new JCheckBox[model.getSize()];
		for(int i = 0; i < result.length; i++) {
			result[i] = model.getElementAt(i);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	public ListModel<JCheckBox> getChecksAsList() {
		return checkBoxList.getModel();
	}
	public String[] getNames() {
		@SuppressWarnings("unchecked")
		ListModel<JCheckBox> model = checkBoxList.getModel();
		String[] result = new String[model.getSize()];
		for(int i = 0; i < result.length; i++) {
			result[i] = model.getElementAt(i).getText();
		}
		return result;
	}
	/**
	 * @return
	 * @see javax.swing.JScrollBar#getMaximum()
	 */
	public int getMaximum() {
		return scrollBar.getMaximum();
	}
	/**
	 * @return
	 * @see javax.swing.JScrollBar#getMinimum()
	 */
	public int getMinimum() {
		return scrollBar.getMinimum();
	}
	/**
	 * @return
	 * @see javax.swing.JScrollBar#getValue()
	 */
	public int getValue() {
		return scrollBar.getValue();
	}
	/**
	 * @param arg0
	 * @see javax.swing.JScrollBar#setMaximum(int)
	 */
	public void setMaximum(int arg0) {
		scrollBar.setMaximum(arg0);
	}
	/**
	 * @param arg0
	 * @see javax.swing.JScrollBar#setMinimum(int)
	 */
	public void setMinimum(int arg0) {
		scrollBar.setMinimum(arg0);
	}
	/**
	 * @param arg0
	 * @see javax.swing.JScrollBar#setValue(int)
	 */
	public void setValue(int arg0) {
		scrollBar.setValue(arg0);
	}
}
