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
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.GlobalSettings;
import mmb.MENU.components.BoundCombo;
import mmb.debug.Debugger;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import static mmb.GlobalSettings.*;

/**
 * @author oskar
 *
 */
public class PanelSettings extends JPanel {
	private static final long serialVersionUID = 3525375553531357014L;
	
	private JTabbedPane tabbedPane;
	private JPanel mods;
	private MultilineLabel lblModInfo;
	private JTextPane textpane;
	private transient Debugger debug = new Debugger("EXTERNAL MODS");
	
	/**
	 * Create the panel.
	 */
	public PanelSettings() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		mods = new JPanel();
		tabbedPane.addTab($res("cgui-mods"), null, mods, null);
		mods.setLayout(new MigLayout("", "[grow]", "[][][]"));
		
		lblModInfo = new MultilineLabel($res("cguis-modhelp"));
		lblModInfo.setPreferredViewportLineCount(5);
		lblModInfo.setPreferredWidthLimit(1000);
		mods.add(lblModInfo, "cell 0 0,growx");
		
		textpane = new JTextPane();
		textpane.setText($res("cguis-load"));
		textpane.setEnabled(false);
		mods.add(textpane, "cell 0 1,grow");
		
		locale = new JPanel();
		tabbedPane.addTab($res("cguis-lang"), null, locale, null);
		locale.setLayout(new MigLayout("", "[][][grow]", "[][][][]"));
		//issue with Locale class
		Locale[] locales = Locale.getAvailableLocales(); //this is making GUIs stuck
		Arrays.sort(locales, 
				(a, b) -> a.toString().compareTo(b.toString()));
		
		lblMustRestart = new JLabel($res("cguis-restart"));
		lblMustRestart.setOpaque(true);
		lblMustRestart.setBackground(Color.ORANGE);
		locale.add(lblMustRestart, "cell 0 0 3 1,growx");
		
		lblLang = new JLabel($res("cguis-lang")+':');
		locale.add(lblLang, "cell 0 1");
		
		comboLang = new BoundCombo<>();
		comboLang.setModel(new DefaultComboBoxModel<>(Locale.getISOLanguages()));
		comboLang.setVariable(GlobalSettings.lang);
		locale.add(comboLang, "cell 2 1,growx");
		
		lblCont = new JLabel($res("cguis-cont")+':');
		locale.add(lblCont, "cell 0 2");
		
		comboCountry = new BoundCombo<>();
		comboCountry.setModel(new DefaultComboBoxModel<>(Locale.getISOCountries()));
		comboCountry.setVariable(GlobalSettings.country);
		locale.add(comboCountry, "cell 2 2,growx");
		
		lblNoSuchSelDefaultCont = new JLabel($res("cguis-nosuch"));
		lblNoSuchSelDefaultCont.setBackground(Color.YELLOW);
		lblNoSuchSelDefaultCont.setOpaque(true);
		locale.add(lblNoSuchSelDefaultCont, "cell 0 3 3 1,growx");
		EventQueue.invokeLater(this::load);
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));

	}
	private final File cfg = new File("ext.txt");
	private JPanel locale;
	private JLabel lblLang;
	private JLabel lblCont;
	private JLabel lblNoSuchSelDefaultCont;
	private JLabel lblMustRestart;
	private BoundCombo<String> comboLang;
	
	private BoundCombo<String>comboCountry;
	
	private void load() {
		
		try {
			boolean avaliable = true;
			if(!cfg.exists()) 
				avaliable = cfg.createNewFile();
			if(avaliable) {
				textpane.setText(new String(Files.readAllBytes(cfg.toPath())));
			}else {
				debug.printl("Failed to create external mods file, external mods WILL not be avaliable");
			}
			
		} catch (IOException e) {
			debug.pstm(e, "THIS EXCEPTION INDICATES PROBLEM WITH FILE SYSTEM OR JAVA");
		}
		
		textpane.setEnabled(true);
	}
	private void save() {
		try(PrintWriter pw = new PrintWriter("ext.txt")) {
			pw.print(textpane.getText());
		} catch (IOException e) {
			debug.pstm(e, "THIS EXCEPTION INDICATES PROBLEM WITH FILE SYSTEM OR JAVA");
		}
	}

}
