/**
 * 
 */
package mmb.content.wireworld.actuator;

import mmb.engine.block.BlockType;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;

/**
 * A dialog box used by the creative block placer ({@link ActuatorPlaceBlock}).
 * The block is selected from the current inventory selection of the player when OK is clicked,
 * or unchanged when Cancel is clicked,
 * or reset to nothing when Remove is clicked
 * @author oskar
 * @see ActuatorPlaceBlock
 * @see BlockSetting
 */
public class SelectBlock extends GUITab {
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
		btnCancel.addActionListener(e -> window.closeWindow(this));
		add(btnCancel);
		
		btnOK = new JButton($res("ok"));
		btnOK.setBackground(Color.GREEN);
		btnOK.addActionListener(e -> {
			ItemEntry item = window.getPlacer().getSelectedItem();
			if(item instanceof BlockType)
				setting.setBlockSetting((BlockType) item);
			window.closeWindow(this);
		});
		add(btnOK);
		
		btnRemove = new JButton($res("remove"));
		btnRemove.setBackground(Color.YELLOW);
		btnRemove.addActionListener(e ->{
			setting.setBlockSetting(null);
			window.closeWindow(this);
		});
		add(btnRemove);
		
		BlockType type = setting.blockSetting();
		lblType = new JLabel($res("wgui-nosel"));
		if(type != null) lblType.setText($res("wgui-curr")+" "+type.title());
		add(lblType);
	}

	@Override
	public void close(WorldWindow window) {
		//nothing
	}
}
