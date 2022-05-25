/**
 * 
 */
package mmb.MENU.main;

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
import java.util.Objects;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.GlobalSettings;
import mmb.DATA.Settings;
import mmb.MENU.components.BoundCombo;
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
		//issue with Locale class
		Locale[] locales = Locale.getAvailableLocales(); //this is making GUIs stuck
		Arrays.sort(locales, 
				(a, b) -> a.toString().compareTo(b.toString()));
		
		lbl6 = new JLabel("For language changes to apply, the game MUST be restarted");
		lbl6.setOpaque(true);
		lbl6.setBackground(Color.ORANGE);
		locale.add(lbl6, "cell 0 0 3 1,growx");
		
		lbl2 = new JLabel("Language:");
		locale.add(lbl2, "cell 0 1");
		
		comboLang = new BoundCombo<>();
		comboLang.setModel(new DefaultComboBoxModel<>(Locale.getISOLanguages()));
		comboLang.setVariable(GlobalSettings.lang);
		locale.add(comboLang, "cell 2 1,growx");
		
		lbl3 = new JLabel("Country:");
		locale.add(lbl3, "cell 0 2");
		
		comboCountry = new BoundCombo<>();
		comboCountry.setModel(new DefaultComboBoxModel<>(Locale.getISOCountries()));
		comboCountry.setVariable(GlobalSettings.country);
		locale.add(comboCountry, "cell 2 2,growx");
		
		lbl4 = new JLabel("If there is no SELECTED language-country combination, the country will be set to default for given language");
		lbl4.setBackground(Color.YELLOW);
		lbl4.setOpaque(true);
		locale.add(lbl4, "cell 0 3 3 1,growx");
		
		lbl5 = new JLabel("Date/time format");
		locale.add(lbl5, "cell 0 4");
		
		comboDTF = new JComboBox<>();
		locale.add(comboDTF, "cell 2 4,growx");
		EventQueue.invokeLater(this::load);
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));

	}
	private final File cfg = new File("ext.txt");
	private JPanel locale;
	private JLabel lbl2;
	private JLabel lbl3;
	private JLabel lbl5;
	private JLabel lbl4;
	private JLabel lbl6;
	private BoundCombo<String> comboLang;
	
	private BoundCombo<String>comboCountry;
	private JComboBox<String> comboDTF;
	
	private void load() {
		
		try {
			boolean avaliable = true;
			if(!cfg.exists()) 
				avaliable = cfg.createNewFile();
			if(avaliable) {
				txtpnLoading.setText(new String(Files.readAllBytes(cfg.toPath())));
			}else {
				debug.printl("Failed to create external mods file, external mods WILL not be avaliable");
			}
			
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
