/**
 * 
 */
package mmb.menu.main;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import static mmb.engine.settings.GlobalSettings.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.engine.debug.Debugger;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.components.BoundCheckBox;
import mmb.menu.components.BoundCombo;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * The settings panel in the main menu
 * @author oskar
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
		
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
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
		
		panelLocale = new JPanel();
		tabbedPane.addTab($res("cguis-lang"), null, panelLocale, null);
		panelLocale.setLayout(new MigLayout("", "[][][grow]", "[][][][]"));
		Locale[] locales = Locale.getAvailableLocales();
		Arrays.sort(locales, 
				(a, b) -> a.toString().compareTo(b.toString()));
		
		lblMustRestart = new JLabel($res("cguis-restart"));
		lblMustRestart.setOpaque(true);
		lblMustRestart.setBackground(Color.ORANGE);
		panelLocale.add(lblMustRestart, "cell 0 0 3 1,growx");
		
		lblLang = new JLabel($res("cguis-lang")+':');
		panelLocale.add(lblLang, "cell 0 1");
		
		comboLang = new BoundCombo<>();
		comboLang.setModel(new DefaultComboBoxModel<>(Locale.getISOLanguages()));
		comboLang.setVariable(GlobalSettings.lang);
		panelLocale.add(comboLang, "cell 2 1,growx");
		
		lblCont = new JLabel($res("cguis-cont")+':');
		panelLocale.add(lblCont, "cell 0 2");
		
		comboCountry = new BoundCombo<>();
		comboCountry.setModel(new DefaultComboBoxModel<>(Locale.getISOCountries()));
		comboCountry.setVariable(GlobalSettings.country);
		panelLocale.add(comboCountry, "cell 2 2,growx");
		
		lblNoSuchSelDefaultCont = new JLabel($res("cguis-nosuch"));
		lblNoSuchSelDefaultCont.setBackground(Color.YELLOW);
		lblNoSuchSelDefaultCont.setOpaque(true);
		panelLocale.add(lblNoSuchSelDefaultCont, "cell 0 3 3 1,growx");
		
		panelUI = new JPanel();
		tabbedPane.addTab($res("cguis-ui"), null, panelUI, null);
		panelUI.setLayout(new MigLayout("", "[31px,grow]", "[15px][]"));
		
		BoundCheckBox checkScale = new BoundCheckBox();
		checkScale.setVariable(sysscale);
		checkScale.setText($res("cguis-scalemethod"));
		panelUI.add(checkScale, "cell 0 0,growx,aligny top");
		
		lblNewLabel = new JLabel($res("cguis-scale"));
		panelUI.add(lblNewLabel, "flowx,cell 0 1");
		
		fieldScale = new JTextField();
		fieldScale.setText(Double.toString(GlobalSettings.uiScale.getDouble()));
		panelUI.add(fieldScale, "cell 0 1,growx");
		fieldScale.setColumns(10);
		
		panelDebug = new JPanel();
		tabbedPane.addTab($res("cguis-debug"), null, panelDebug, null);
		panelDebug.setLayout(new MigLayout("", "[]", "[]"));
		
		boundCheckBox = new BoundCheckBox($res("cguis-dumpbundles"));
		boundCheckBox.setVariable(dumpBundles);
		panelDebug.add(boundCheckBox, "cell 0 0");
		
		AtomicBoolean recovery = new AtomicBoolean();
		fieldScale.addActionListener(e -> {
			if(recovery.get()) return;
			String s = fieldScale.getText();
			try {
				if(s == null) throw new IllegalArgumentException("invalid value");
				double newScale = Double.parseDouble(s);
				if(Double.isNaN(newScale)) throw new IllegalArgumentException("NaN");
				if(newScale <= 0) throw new IllegalArgumentException("<=0");
				if(Double.isInfinite(newScale)) throw new IllegalArgumentException("Infinite");
			}catch(Exception ex) {
				debug.stacktraceError(ex, "Invalid scaling factor");
				JOptionPane.showMessageDialog(new JFrame(), $res("cguis-scalerr")+s+" : "+ex.getClass()+": "+ex.getMessage(), $res("cguis-scale"),
				        JOptionPane.ERROR_MESSAGE);
				recovery.set(true);
				fieldScale.setText(s);
				recovery.set(false);
			}
		});
		
		EventQueue.invokeLater(this::load);
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));

	}
	private final File cfg = new File("ext.txt");
	private JPanel panelLocale;
	private JLabel lblLang;
	private JLabel lblCont;
	private JLabel lblNoSuchSelDefaultCont;
	private JLabel lblMustRestart;
	private BoundCombo<String> comboLang;
	
	private BoundCombo<String>comboCountry;
	private JPanel panelUI;
	private JTextField fieldScale;
	private JLabel lblNewLabel;
	private JPanel panelDebug;
	private BoundCheckBox boundCheckBox;
	
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
			debug.stacktraceError(e, "THIS EXCEPTION INDICATES PROBLEM WITH FILE SYSTEM OR JAVA");
		}
		
		textpane.setEnabled(true);
	}
	private void save() {
		try(PrintWriter pw = new PrintWriter("ext.txt")) {
			pw.print(textpane.getText());
		} catch (IOException e) {
			debug.stacktraceError(e, "THIS EXCEPTION INDICATES PROBLEM WITH FILE SYSTEM OR JAVA");
		}
	}
}
