package mmb.ui.shop;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JList;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.csv.*;
import org.apache.commons.io.IOUtils;

import mmb.DATA.file.AdvancedFile;
import mmb.DATA.file.FileGetter;
import mmb.debug.Debugger;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModShop extends JFrame {
	private final JPanel contentPane;
	private final Debugger debug;
	private final java.awt.List list;
	private final ArrayList<Mod> mods = new ArrayList<Mod>();
	/**
	 * Create the frame.
	 */
	
	public static class Mod{
		public String dl;
		public String name;
		public void download(){
			Debugger debug2 = new Debugger("DOWNLOAD-"+name);
			debug2.printl("Downloading "+dl);
			String[] split = dl.split("\\\\");
			split[0]=split[split.length-1];
			split = split[0].split("/");
			String newName = split[split.length-1];
			try {
				AdvancedFile target = FileGetter.getFile("mods/"+newName);
				target.create();
				AdvancedFile source = FileGetter.getFile(dl);
				source.copyTo(target);
				debug2.printl("Download successful");
			} catch (Exception e) {
				debug2.pstm(e, "Couldn't download file");
			}
		}
	}
	public ModShop() {
		debug = new Debugger("SHOP");
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[238px,grow][174px][grow]", "[245px,grow]"));
		
		JPanel settings = new JPanel();
		contentPane.add(settings, "cell 0 0,grow");
		settings.setLayout(new MigLayout("", "[]", "[]"));
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				int id = list.getSelectedIndex();
				mods.get(id).download();
			}
		});
		settings.add(btnDownload, "cell 0 0");
		
		list = new java.awt.List();
		contentPane.add(list, "cell 1 0");
		
		JPanel info = new JPanel();
		contentPane.add(info, "cell 2 0,grow");
		info.setLayout(new MigLayout("", "[46px]", "[14px][]"));
		
		JLabel modSize = new JLabel("Download size");
		info.add(modSize, "cell 0 0,alignx left,aligny top");
		
		JLabel lblAgeRating = new JLabel("Age rating");
		info.add(lblAgeRating, "cell 0 1");
		
		try {
			addData(FileGetter.getFile("database.txt").getInputStream());
		} catch (Exception e) {
			debug.pstm(e, "Couldn't download the database");
		}
	}
	
	private void addData(InputStream source) {
		Reader reader = new InputStreamReader(source);
		//Add downloadable mods
		try {
			List<CSVRecord> data = CSVFormat.DEFAULT.withAllowMissingColumnNames().parse(reader).getRecords();
			data.forEach(this::addRecord);
			
		} catch (IOException e) {
			debug.pstm(e, "Failed to download");
		}
	}
	
	/**
	 * Required:
	 * download - download link
	 * 
	 * Supported:
	 * name - mod name
	 * 
	 * name download
	 * @param record
	 */
	private void addRecord(CSVRecord record) {
		String name = record.get(0);
		String dl = record.get(1);
		if(name == null) name = "";
		Mod mod = new Mod();
		mod.dl = dl;
		mod.name = name;
		mods.add(mod);
		list.add(name);
	}
	
	public static String getRecordSafe(CSVRecord r, String s) {
		if(r.isMapped(s)) return r.get(s);
		return null;
	}
	
}
