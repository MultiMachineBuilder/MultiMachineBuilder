/**
 * 
 */
package mmb.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.debug.Debugger;
import net.miginfocom.swing.MigLayout;
import java.awt.List;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;

/**
 * @author oskar
 *
 */
public class ExternalMods extends JDialog {

	private Debugger debug = new Debugger("UI-EXTERNAL");
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private List list;
	private ExternalMods that = this;

	/**
	 * Create the dialog.
	 */
	public ExternalMods() {
		setTitle("External files");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
			list = new List();
			contentPanel.add(list);
			
			JLabel lblNewLabel = new JLabel("<html>Add only mods from trusted sources.<br>\r\nAny mods here must be either local file or downloadable file.<br>\r\nWebpages and websites won't work.</html>");
			contentPanel.add(lblNewLabel, BorderLayout.NORTH);
			load();
		
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[47px][65px][][grow][]", "[23px,grow]"));
			
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					that.save();
					that.setVisible(false);
					that.dispose();
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton, "cell 0 0,alignx left,aligny top");
			getRootPane().setDefaultButton(okButton);
		
		
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					that.setVisible(false);
					that.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton, "cell 1 0,alignx left,aligny top");
		
		
			JButton btnAdd = new JButton("Add:");
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					list.add(textField.getText());
				}
			});
			buttonPane.add(btnAdd, "cell 2 0");
		
		
			textField = new JTextField();
			buttonPane.add(textField, "cell 3 0,growx");
			textField.setColumns(10);
			
			JButton btnDelete = new JButton("Delete");
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					list.delItem(list.getSelectedIndex());
				}
			});
			buttonPane.add(btnDelete, "cell 4 0");
			
		
	}
	private void save() {
		StringBuilder text = new StringBuilder();
		String[] data = list.getItems();
		int length = data.length;
		for(int i = 0; i < length; i++) {
			text.append(data[i]);
			if(i < length - 1) {
				text.append("\n");
			}
		}
		
		try {
			PrintWriter save = new PrintWriter("ext.txt");
			save.write(text.toString());
			save.flush();
			save.close();
		} catch (FileNotFoundException e) {
			debug.pstm(e, "This message should never appear - report bug in Java");
		}
	}
	private void load() {
		try {
			String[] datas = new String(Files.readAllBytes(Paths.get("ext.txt"))).split("\n");
			for(int i = 0; i < datas.length; i++) {
				list.add(datas[i]);
			}
		} catch (IOException e) {
			debug.pstm(e, "Unable to display list of external files");
			this.setVisible(false);
			this.dispose();
		}
	}
}
