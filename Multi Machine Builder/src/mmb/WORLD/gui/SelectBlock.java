/**
 * 
 */
package mmb.WORLD.gui;

import javax.swing.JPanel;

import mmb.BEANS.BlockSetting;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.window.WorldWindow;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Color;

/**
 * @author oskar
 *
 */
public class SelectBlock extends JPanel {
	private static final long serialVersionUID = -5267127312725839315L;
	
	private JLabel lblNewLabel;
	private JButton btnCancel;
	private JButton btnOK;
	private JButton btnRemove;
	
	private WorldWindow window;
	private JLabel lblType;
	public SelectBlock(BlockSetting setting, WorldWindow window) {
		this.window = window;
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		lblNewLabel = new JLabel("Select a block type");
		add(lblNewLabel);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBackground(Color.RED);
		btnCancel.addActionListener(e -> close());
		add(btnCancel);
		
		btnOK = new JButton("OK");
		btnOK.setBackground(Color.GREEN);
		btnOK.addActionListener(e -> {
			Placer placer = window.getPlacer().getPlacer();
			if(placer instanceof BlockPlacer)
				setting.setBlockSetting(((BlockPlacer) placer).block);
			close();
		});
		add(btnOK);
		
		btnRemove = new JButton("Remove");
		btnRemove.setBackground(Color.YELLOW);
		btnRemove.addActionListener(e ->{
			setting.setBlockSetting(null);
			close();
		});
		add(btnRemove);
		
		BlockType type = setting.getBlockSetting();
		lblType = new JLabel("None selected");
		if(type != null) lblType.setText("Current: "+type.title());
		add(lblType);
	}

	private void close() {
		window.closeDialogWindow(this);
	}
}
