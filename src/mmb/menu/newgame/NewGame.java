/**
 * 
 */
package mmb.menu.newgame;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.content.ContentsBlocks;
import mmb.content.ContentsItems;
import mmb.engine.debug.Debugger;
import mmb.engine.generator.Generator;
import mmb.engine.generator.Generators;
import mmb.engine.inv.Inventory;
import mmb.engine.json.JsonTool;
import mmb.engine.worlds.universe.Universe;
import mmb.engine.worlds.world.World;
import mmb.menu.FullScreen;
import mmb.menu.MMBFrame;
import mmb.menu.main.MainMenu;
import mmb.menu.main.PanelSaves;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;

/**
 * A world creation GUI
 * @author oskar
 */
@SuppressWarnings("serial")
public class NewGame extends MMBFrame {

	private final JPanel contentPanel = new JPanel();
	private final JTextField txtHeight;
	private final JTextField txtWidth;
	private final JTextField txtName;
	private final transient Debugger debug = new Debugger("NewGame");
	private int w;
	private int h;
	private int csize;
	private JList<Generator> list;
	private JTextField txtcsize;
	private JTextField txtSeed;
	private void getWorldSize() {
		w = 0;
		h = 0;
		csize = 0;
		try {
			w = Integer.parseInt(txtWidth.getText());
			h = Integer.parseInt(txtWidth.getText());
			csize = Integer.parseInt(txtcsize.getText());
			Long.parseLong(txtSeed.getText());
		} catch (NumberFormatException e2) {
			w = -2;
			h = 0;
			csize = 0;
			debug.stacktraceError(e2,
					"Incorrect dimensions: "+txtWidth.getText()+
					","+txtHeight.getText()+
					", chunk size: "+txtcsize.getText()+
					", seed: "+txtSeed.getText()
			);
			return;
		}
		if(w < 0 || h < 0 || csize < 0) {
			w = -1;
			h = 0;
			csize = 0;
		}
	}
	private static final Random r = new Random();
	private JCheckBox checkCreative;
	/**
	 * Create the dialog.
	 */
	public NewGame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(@SuppressWarnings("null") WindowEvent arg0) {
				dispose();
			}
		});
		setTitle($res("ngui-title"));
		setBounds(100, 100, 610, 432);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][grow]"));
		
			JLabel lblHeight = new JLabel($res("ngui-h"));
			contentPanel.add(lblHeight, "cell 0 0,alignx trailing");
		
			txtHeight = new JTextField();
			txtHeight.setText("300");
			contentPanel.add(txtHeight, "cell 1 0,growx");
			txtHeight.setColumns(10);
		
			JLabel lblWidth = new JLabel($res("ngui-w"));
			contentPanel.add(lblWidth, "cell 0 1,alignx trailing");
		
			txtWidth = new JTextField();
			txtWidth.setText("300");
			contentPanel.add(txtWidth, "cell 1 1,growx");
			txtWidth.setColumns(10);
		
			JLabel lblName = new JLabel($res("ngui-n"));
			contentPanel.add(lblName, "cell 0 2,alignx trailing");
		
			txtName = new JTextField();
			txtName.setText("Name");
			contentPanel.add(txtName, "cell 1 2,growx");
			txtName.setColumns(10);
			
			JLabel lblChunkSize = new JLabel($res("ngui-c"));
			contentPanel.add(lblChunkSize, "cell 0 3,alignx trailing");
			
			txtcsize = new JTextField();
			txtcsize.setText("32");
			contentPanel.add(txtcsize, "cell 1 3,growx");
			txtcsize.setColumns(10);
			
			JLabel lblSeed = new JLabel($res("ngui-s"));
			contentPanel.add(lblSeed, "cell 0 4,alignx trailing");
			
			txtSeed = new JTextField();
			contentPanel.add(txtSeed, "cell 1 4,growx");
			txtSeed.setColumns(10);
			txtSeed.setText(Long.toString(r.nextLong()));
			
			JLabel lblWgen = new JLabel($res("ngui-g"));
			contentPanel.add(lblWgen, "cell 0 5");
			
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 1 5,grow");
			
			list = new JList<>();
			list.setModel(Generators.generators);
			list.setCellRenderer(new CellRenderer());
			scrollPane.setViewportView(list);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
			JButton okButton = new JButton($res("ok"));
			okButton.addActionListener(e -> save());
			
			checkCreative = new JCheckBox($res("wgui-creamode"));
			buttonPane.add(checkCreative);
			
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		
			JButton cancelButton = new JButton($res("cancel"));
			cancelButton.addActionListener(arg0 -> dispose());
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
	}
	
	private class CellRenderer extends JLabel implements ListCellRenderer<Generator>{
		public CellRenderer() {setOpaque(true);}
		@Override
		public Component getListCellRendererComponent(
			@SuppressWarnings("null") JList<? extends Generator> list,
			@SuppressWarnings("null") Generator item, int index,
			boolean isSelected, boolean cellHasFocus
		){
			setText(item.title());
			
			if (isSelected) {
			    setBackground(list.getSelectionBackground());
			    setForeground(list.getSelectionForeground());
			} else {
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
			return this;
		}
	}

	private void save() {
		Generator gen = list.getSelectedValue();
		if(gen == null) {
			debug.printl("generator is not selected");
			return;
		}
		String n = txtName.getText();
		debug.printl("World name: "+n);
		getWorldSize();
		if(w == -1) debug.printl("Incorrect dimensions: "+txtWidth.getText()+","+txtHeight.getText());
		if(w < 0) return;
		
		
		//Fill and create the map
		int ww = (2*w)+1;
		int hh = (2*h)+1;
		World main = new World(ww, hh, -w, -h);
		gen.generate(main, csize);

		//Initialize the player
		Inventory inv = main.player.inv;
		inv.insert(ContentsBlocks.logs, 2);
		inv.insert(ContentsItems.pickVW.create(), 1);
		inv.insert(ContentsItems.bucket, 1);
		main.player.creative.setValue(checkCreative.isSelected());
		
		//Create and set up the world
		Universe world = new Universe();
		world.setMain(main);
		
		//Write to a new file
		File newFile = new File("maps/"+n+".mworld");
		if(newFile.exists()) {
			debug.printl('"'+n+'"'+" already exists");
			dispose();
			return;
		}
		try(OutputStream os = new FileOutputStream(newFile)) {
			JsonNode object = world.save();
			String text = JsonTool.save(object);
			byte[] bin = text.getBytes();
			os.write(bin);
			os.flush();
		}catch (Exception e1) {
			debug.stacktraceError(e1, "Failed to write the new world.");
			dispose();
			return;
		}
						
		debug.printl("Successfully created "+n);
		
		world.destroy();
		
		FullScreen.setWindow(MainMenu.INSTANCE);
		PanelSaves.INSTANCE.refresh();
	}	
	@Override
	public void destroy() {
		FullScreen.setWindow(MainMenu.INSTANCE);
	}
}
