/**
 * 
 */
package mmb.MENU.settings;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import mmb.debug.Debugger;

import javax.swing.JButton;

/**
 * @author oskar
 *
 */
public class PanelSettings extends JPanel {
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JTextPane txtpnLoading;
	private Debugger debug = new Debugger("EXTERNAL MODS");

	/**
	 * Create the panel.
	 */
	public PanelSettings() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		tabbedPane.addTab("Mods", null, panel, null);
		panel.setLayout(new MigLayout("", "[grow]", "[][][]"));
		
		lblNewLabel = new JLabel("<html>External Mods<br>Add only mods from trusted sources.<br>Any mods here must be either local file or downloadable file.<br>Webpages and websites won't work.</html>");
		panel.add(lblNewLabel, "cell 0 0,growx");
		
		txtpnLoading = new JTextPane();
		txtpnLoading.setText("Loading...");
		txtpnLoading.setEnabled(false);
		panel.add(txtpnLoading, "cell 0 1,grow");
		EventQueue.invokeLater(this::load);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> this.save()));

	}
	private final File cfg = new File("ext.txt");
	private void load() {
		try {
			if(!cfg.exists()) cfg.createNewFile();
			String data = new String(Files.readAllBytes(cfg.toPath()));
			txtpnLoading.setText(data);
		} catch (IOException e) {
			debug.pstm(e, "THIS EXCEPTION INDICATES PROBLEM WITH FILE SYSTEM OR JAVA");
		}
		txtpnLoading.setEnabled(true);
	}
	private void save() {
		try(PrintWriter pw = new PrintWriter("ext.txt")) {
			pw.print(txtpnLoading.getText());
		} catch (IOException e) {
			debug.pstm(e, "THIS EXCEPTION INDICATES PROBLEM WITH FILE SYSTEM OR JAVA");
		}
	}

}
