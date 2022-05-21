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
import java.util.Arrays;
import java.util.Locale;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.debug.Debugger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;

/**
 * @author oskar
 *
 */
public class PanelSettings extends JPanel {
	private static final long serialVersionUID = 3525375553531357014L;
	
	private JTabbedPane tabbedPane;
	private JPanel mods;
	private MultilineLabel lblNewLabel;
	private JTextPane txtpnLoading;
	private transient Debugger debug = new Debugger("EXTERNAL MODS");

	/**
	 * Create the panel.
	 */
	public PanelSettings() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		mods = new JPanel();
		tabbedPane.addTab("Mods", null, mods, null);
		mods.setLayout(new MigLayout("", "[grow]", "[][][]"));
		
		lblNewLabel = new MultilineLabel("External Mods\nAdd only mods from trusted sources.\nAny mods here must be either local file or downloadable file.\nWebpages and websites won't work.");
		lblNewLabel.setPreferredViewportLineCount(5);
		lblNewLabel.setPreferredWidthLimit(1000);
		mods.add(lblNewLabel, "cell 0 0,growx");
		
		txtpnLoading = new JTextPane();
		txtpnLoading.setText("Loading...");
		txtpnLoading.setEnabled(false);
		mods.add(txtpnLoading, "cell 0 1,grow");
		
		locale = new JPanel();
		tabbedPane.addTab("Language", null, locale, null);
		locale.setLayout(new MigLayout("", "[][][grow]", "[][][][][][]"));
		
		lblQuckLoc = new JLabel("Preinstalled locales");
		locale.add(lblQuckLoc, "cell 0 0");
		
		btnQuickLoc = new JButton("Select:");
		locale.add(btnQuickLoc, "cell 1 0");
		
		comboLocales = new JComboBox<Locale>();
		//issue with Locale class
		Locale[] locales = Locale.getAvailableLocales(); //this is stuck
		Arrays.sort(locales, (a, b) -> a.toString().compareTo(b.toString()));
		comboLocales.setModel(new DefaultComboBoxModel<>(locales));
		locale.add(comboLocales, "cell 2 0,growx");
		
		lblNewLabel_1 = new JLabel("Language:");
		locale.add(lblNewLabel_1, "cell 0 1");
		
		comboLang = new JComboBox<>();
		comboLang.setModel(new DefaultComboBoxModel<>(Locale.getISOLanguages()));
		locale.add(comboLang, "cell 2 1,growx");
		
		lblNewLabel_2 = new JLabel("Country:");
		locale.add(lblNewLabel_2, "cell 0 2");
		
		comboCountry = new JComboBox();
		comboCountry.setModel(new DefaultComboBoxModel<>(Locale.getISOCountries()));
		locale.add(comboCountry, "cell 2 2,growx");
		
		lblNewLabel_4 = new JLabel("If there is no SELECTED language-country combination, the country will be set to default for given language");
		lblNewLabel_4.setBackground(Color.YELLOW);
		lblNewLabel_4.setOpaque(true);
		locale.add(lblNewLabel_4, "cell 0 3 3 1,growx");
		
		lblNewLabel_3 = new JLabel("Date/time format");
		locale.add(lblNewLabel_3, "cell 0 4");
		
		comboDTF = new JComboBox();
		locale.add(comboDTF, "cell 2 4,growx");
		
		lblNewLabel_5 = new JLabel("For language changes to apply, the game MUST be restarted");
		lblNewLabel_5.setOpaque(true);
		lblNewLabel_5.setBackground(Color.ORANGE);
		locale.add(lblNewLabel_5, "cell 0 5 3 1,growx");
		EventQueue.invokeLater(this::load);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> this.save()));

	}
	private final File cfg = new File("ext.txt");
	private JPanel locale;
	private JLabel lblQuckLoc;
	private JButton btnQuickLoc;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JComboBox<Locale> comboLocales;
	private JComboBox<String> comboLang;
	
	private JComboBox<String> comboCountry;
	private JComboBox<String> comboDTF;
	
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
