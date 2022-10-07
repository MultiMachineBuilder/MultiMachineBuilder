/**
 * 
 */
package mmb.menu.world;

import javax.swing.JPanel;

import mmb.beans.BlockSetting;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.BlockType;
import mmb.world.items.ItemEntry;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import static mmb.GlobalSettings.$res;

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
		
		lblNewLabel = new JLabel($res("wguim-selblock"));
		add(lblNewLabel);
		
		btnCancel = new JButton($res("cancel"));
		btnCancel.setBackground(Color.RED);
		btnCancel.addActionListener(e -> close());
		add(btnCancel);
		
		btnOK = new JButton($res("ok"));
		btnOK.setBackground(Color.GREEN);
		btnOK.addActionListener(e -> {
			ItemEntry item = window.getPlacer().getSelectedValue().item();
			if(item instanceof BlockType)
				setting.setBlockSetting((BlockType) item);
			close();
		});
		add(btnOK);
		
		btnRemove = new JButton($res("remove"));
		btnRemove.setBackground(Color.YELLOW);
		btnRemove.addActionListener(e ->{
			setting.setBlockSetting(null);
			close();
		});
		add(btnRemove);
		
		BlockType type = setting.getBlockSetting();
		lblType = new JLabel($res("wgui-nosel"));
		if(type != null) lblType.setText($res("wgui-curr")+" "+type.title());
		add(lblType);
	}

	private void close() {
		window.closeDialogWindow(this);
	}
}
