package mmb.ui.game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.vfs2.FileExtensionSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;

import mmb.debug.Debugger;
import mmb.files.FileGetter;
import mmb.files.saves.Save;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.List;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class SelectGame extends JFrame {

	private JPanel contentPane;
	private Debugger debug = new Debugger("SELECT A SAVE");
	public java.util.List<Save> saves = new ArrayList<Save>();

	/**
	 * Create the frame.
	 */
	public SelectGame() {
		setTitle("Save manager");
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[94.00,grow][grow]"));
		
		List list = new List();
		contentPane.add(list, "cell 0 0,grow");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("", "[][][][][][]", "[][][]"));
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NewGame().setVisible(true);
			}
		});
		panel.add(btnNew, "cell 0 0");
		
		JButton btnTemplate = new JButton("Template");
		btnTemplate.setEnabled(false);
		panel.add(btnTemplate, "cell 1 0");
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.setEnabled(false);
		panel.add(btnUpload, "cell 4 0");
		
		JButton btnSettings = new JButton("Settings");
		btnSettings.setEnabled(false);
		panel.add(btnSettings, "cell 0 1");
		
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete, "cell 1 1");
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Save s = saves.get(list.getSelectedIndex());
				try {
					new WorldFrame(s.file.getContent()).setVisible(true);
					debug.printl("Successfully opened "+s.name);
				} catch (FileSystemException e) {
					debug.pstm(e, "Failed to open "+s.name);
				}
			}
		});
		panel.add(btnPlay, "cell 0 2");
		
		try {
			FileObject saves2 = FileGetter.getFileRelative("maps/");
			FileSelector sel = new FileExtensionSelector("machine1world");
			FileObject[] files = saves2.findFiles(sel);
			for(int i = 0; i < files.length; i++) {
				Save save = new Save(files[i]);
				saves.add(save);
				list.add(save.name);
				debug.printl("Found save: "+save.name);
			}
			debug.printl("Found "+files.length+" saves");
			
		} catch (FileSystemException e) {
			debug.pstm(e, "Failed to get savelist");
		}
	}

}
