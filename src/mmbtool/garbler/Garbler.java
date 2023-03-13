/**
 * 
 */
package mmbtool.garbler;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import mmb.Nil;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Properties;
import java.awt.event.ActionEvent;

/**
 *
 * @author oskar
 *
 */
public class Garbler extends JFrame {
	private static final long serialVersionUID = -8102041785381014214L;
	
	private JPanel contentPane;
	@Nil private File source;
	JFileChooser jfc = new JFileChooser();

	/** Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
				try {
					Garbler frame = new Garbler();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
	}

	/** Create the frame. */
	public Garbler() {
		setTitle("Text garbler");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JButton load = new JButton("LOAD");
		load.addActionListener(e -> load());
		contentPane.add(load);
		
		JButton save = new JButton("SAVE");
		save.addActionListener(e -> save());
		contentPane.add(save);
	}

	private void load() {
		int state = jfc.showOpenDialog(null);
		source = (state == JFileChooser.APPROVE_OPTION?jfc.getSelectedFile():null);
		if(state != JFileChooser.APPROVE_OPTION) JOptionPane.showMessageDialog(null, "File not selected", "Error", JOptionPane.ERROR_MESSAGE);
	}
	private void save() {
		//Get the file
		int state = jfc.showSaveDialog(null);
		boolean err = false;
		File dest = jfc.getSelectedFile();
		if(state != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "Invalid destination", "Error", JOptionPane.ERROR_MESSAGE);
			err = true;
		}
		if(source == null) {
			JOptionPane.showMessageDialog(null, "Invalid source", "Error", JOptionPane.ERROR_MESSAGE);
			err = true;
		}
		if(err) return;
		
		//Load the file
		Properties props = new Properties();
		try(InputStream in = new FileInputStream(source)) {
			props.load(in);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e, "Error opening a file", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//Process the file
		Properties props2 = new Properties();
		for(Entry<Object, Object> entry: props.entrySet()) {
			Object key = entry.getKey();
			String value = entry.getValue().toString();
			String value2 = garbleString(value);
			props2.put(key, value2);
		}
		
		//Save the file
		try(Writer w = new FileWriter(dest, StandardCharsets.UTF_8)){
			props2.store(w, "GARBLED BY THE MMB GARBLER");
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e, "Error saving a file", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static String garbleString(String s) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length(); i++) {
			char c1 = s.charAt(i);
			if(c1 < 256) {
				boolean test = i+1 < s.length();
				char c2 = 0;
				if(test) {
					c2 = s.charAt(i+1);
				}				
				i++;
				char c = (char) ((c1*256)+c2);
				sb.append(c);
			}else {
				sb.append(c1);
			}
		}
		return sb.toString();
	}
}
