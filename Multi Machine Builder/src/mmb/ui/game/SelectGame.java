package mmb.ui.game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.DATA.file.AdvancedFile;
import mmb.DATA.file.LocalFile;
import mmb.debug.Debugger;
import mmb.files.saves.Save;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.List;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class SelectGame extends JFrame {

	private final JPanel contentPane;
	private final Debugger debug = new Debugger("SELECT A SAVE");
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
		panel.setLayout(new MigLayout("", "[][][][][][]", "[][][][]"));
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NewGame().setVisible(true);
			}
		});
		panel.add(btnNew, "cell 0 0");
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Save s = saves.get(list.getSelectedIndex());
				new WorldFrame(s.file).setVisible(true);
				debug.printl("Successfully opened "+s.name);
			}
		});
		panel.add(btnPlay, "cell 0 1");
		
		LocalFile saves2 = new LocalFile("maps/");
		AdvancedFile[] files;
		try {
			files = saves2.children();
			for(int i = 0; i < files.length; i++) {
				if(files[i].name().endsWith(".mworld")){
					Save save = new Save(files[i]);
					saves.add(save);
					list.add(save.name);
					debug.printl("Found save: "+save.name);
				}
			}
			debug.printl("Found "+files.length+" saves");
		} catch (Exception e) {
			debug.pstm(e, "THIS EXCEPTION SHOULD NOT HAPPEN");
		}
	}

}
