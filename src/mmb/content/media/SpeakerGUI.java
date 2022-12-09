/**
 * 
 */
package mmbgame.media;

import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmbeng.GlobalSettings;
import mmbeng.debug.Debugger;
import mmbeng.sound.Sounds;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Color;
import java.util.*;

/**
 * @author oskar
 *
 */
public class SpeakerGUI extends GUITab {
	private static final String strNoSel = GlobalSettings.$res("multi-nosoundsel");
	private static final String strAutoSel = GlobalSettings.$res("multi-autosoundsel");
	private static final String soundsel = GlobalSettings.$res("multi-soundsel");
	private static final String selsound = GlobalSettings.$res("multi-selsound");
	private static final String play = GlobalSettings.$res("multi-play");
	private static final String stop = GlobalSettings.$res("multi-stop");
	private static final String begin = GlobalSettings.$res("multi-begin");
	private static final String pause = GlobalSettings.$res("multi-pause");
	private static final String unsel = GlobalSettings.$res("multi-unsel");
	private static final String resume = GlobalSettings.$res("multi-resume");
	private static final Debugger debug = new Debugger("SPEAKER GUI");
	private JComboBox<String> comboSelection;
	private JLabel lblCurrPlay;
	private final Speaker speaker;
	public SpeakerGUI(Speaker spk, WorldWindow win) {
		speaker = spk;
		setLayout(new MigLayout("", "[grow 200][grow 300]", "[][][][]"));
		
		lblCurrPlay = new JLabel(soundsel);
		add(lblCurrPlay, "cell 0 0,growx");
		
		JButton btnUnsel = new JButton();
		resetSel();
		btnUnsel.setBackground(new Color(255, 69, 0));
		add(btnUnsel, "cell 1 0,growx");
		
		JLabel lblSelect = new JLabel(selsound);
		add(lblSelect, "cell 0 1,growx");
		
		comboSelection = new JComboBox<>();
		int count = Sounds.sounds.size();
		String[] sounds = new String[count];
		Sounds.sounds.keySet().toArray(sounds);
		
		comboSelection.setModel(new DefaultComboBoxModel<>(sounds));
		comboSelection.addActionListener(e -> {
			//Handle the sound change
			String sel = (String)comboSelection.getSelectedItem();
			spk.setSelection(sel); //gets stuck
			resetSel();
		});
		
		add(comboSelection, "cell 1 1,growx");
		
		JButton btnRew = new JButton(begin);
		btnRew.setBackground(Color.YELLOW);
		btnRew.addActionListener(e -> spk.rewind());
		add(btnRew, "flowx,cell 0 2,growx");
		
		JButton btnPlay = new JButton(play);
		btnPlay.setBackground(Color.GREEN);
		btnPlay.addActionListener(e -> spk.play());
		add(btnPlay, "flowx,cell 1 2,growx");
		
		JButton btnPause = new JButton(pause);
		btnPause.setBackground(Color.ORANGE);
		btnPause.addActionListener(e -> spk.pause());
		add(btnPause, "cell 1 2,growx");
		
		JButton btnStop = new JButton(stop);
		btnStop.setBackground(Color.RED);
		btnStop.addActionListener(e -> spk.stop());
		add(btnStop, "cell 1 2,growx");
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(new Color(220, 20, 60));
		btnExit.addActionListener(e -> win.closeWindow(this));
		add(btnExit, "cell 0 3,growx");
		
		JComboBox<String> comboMode = new JComboBox<>();
		String[] sels = Arrays.stream(SpeakerMode.values()).map(Enum::name).toArray(size -> new String[size]);
		comboMode.setModel(new DefaultComboBoxModel<>(sels));
		comboMode.addActionListener(e -> {
			String selection = (String) comboMode.getSelectedItem();
			SpeakerMode mode = SpeakerMode.valueOf(selection);
			spk.setMode(mode);
			debug.printl("New mode: "+mode);
			debug.printl("Confirm mode: "+spk.getMode());
		}); //speaker mode not set properly
		debug.printl("Existing mode: "+spk.getMode());
		comboMode.setSelectedItem(spk.getMode());
		add(comboMode, "cell 1 3,growx");
		
		JButton btnResume = new JButton(resume);
		btnResume.setBackground(new Color(0, 128, 0));
		btnResume.addActionListener(e -> spk.resume());
		add(btnResume, "cell 0 2,growx");
	}
	
	private void resetSel() {
		if(speaker.getSelection() == null) {
			if(speaker.getSound() == null) {
				lblCurrPlay.setText(strNoSel);
			}else {
				lblCurrPlay.setText(strAutoSel);
			}
		}else {
			lblCurrPlay.setText(soundsel + " " + speaker.getSelection());
			
		}
	}

	@Override
	public void close(WorldWindow window) {
		// unused
	}

}
