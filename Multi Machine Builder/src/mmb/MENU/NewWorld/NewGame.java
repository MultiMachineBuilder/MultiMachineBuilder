/**
 * 
 */
package mmb.MENU.NewWorld;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.JsonNode;
import mmb.DATA.json.JsonTool;
import mmb.MENU.main.PanelSaves;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author oskar
 *
 */
@SuppressWarnings("serial")
public class NewGame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final JTextField txtHeight;
	private final JTextField txtWidth;
	private final JTextField txtName;
	private final transient Debugger debug = new Debugger("NewGame");
	private int w;
	private int h;

	private void getWorldSize() {
		w = 0;
		h = 0;
		try {
			w = Integer.valueOf(txtWidth.getText());
			h = Integer.valueOf(txtWidth.getText());
		} catch (NumberFormatException e2) {
			w = -2;
			h = 0;
			debug.pstm(e2, "Incorrect dimensions: "+txtWidth.getText()+","+txtHeight.getText());
			return;
		}
		if(w < 0 || h < 0) {
			w = -1;
			h = 0;
		}
	}
	/**
	 * Create the dialog.
	 */
	public NewGame() {
		setTitle("Create a new world");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
			JLabel lblHeight = new JLabel("Height");
			contentPanel.add(lblHeight, "cell 0 0,alignx trailing");
		
			txtHeight = new JTextField();
			txtHeight.setText("Height");
			contentPanel.add(txtHeight, "cell 1 0,growx");
			txtHeight.setColumns(10);
		
			JLabel lblWidth = new JLabel("Width");
			contentPanel.add(lblWidth, "cell 0 1,alignx trailing");
		
			txtWidth = new JTextField();
			txtWidth.setText("Width");
			contentPanel.add(txtWidth, "cell 1 1,growx");
			txtWidth.setColumns(10);
		
			JLabel lblName = new JLabel("Name");
			contentPanel.add(lblName, "cell 0 2,alignx trailing");
		
			txtName = new JTextField();
			txtName.setText("Name");
			contentPanel.add(txtName, "cell 1 2,growx");
			txtName.setColumns(10);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
			JButton okButton = new JButton("OK");
			okButton.addActionListener(e ->{
				World world = null;
				getWorldSize();
				if(w == -1) {
					debug.printl("Incorrect dimensions: "+txtWidth.getText()+","+txtHeight.getText());
					return;
				}
				String n = txtName.getText();
				//Fill and create the map
				int ww = (2*w)+1;
				int hh = (2*h)+1;
				BlockEntry[][] ents = new BlockEntry[ww][hh];
				BlockMap main = new BlockMap(ents, -w, -h);
				for(int i = 0, x = -w; i < ww; i++, x++) {
					for(int j = 0, y = -h; j < hh; j++, y++) {
						ents[i][j] = BlockEntry.defaultProperties(x, y, main, ContentsBlocks.grass);
					}
				}
				
				//Create and set up the world
				world = new World();
				world.setMain(main);
				
				
				//Write to a new file
				try {
					File newFile = new File("maps/"+n+".mworld");
					if(!newFile.createNewFile()) {
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
					}
				}catch (Exception e1) {
					debug.pstm(e1, "Failed to write the new world.");
					dispose();
					return;
				}
				
				debug.printl("Successfully created "+n);
				dispose();
				
				world.destroy();
				PanelSaves.INSTANCE.refresh();
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(arg0 -> dispose());
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
			
		pack();
	}

}
