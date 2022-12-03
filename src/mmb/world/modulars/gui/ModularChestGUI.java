/**
 * 
 */
package mmb.world.modulars.gui;

import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.world.modulars.BlockModuleOrCore;
import mmb.world.modulars.ModularBlock;
import mmb.world.modulars.Slot;
import mmb.world.modulars.Slot.CoreSlot;
import mmb.world.rotate.Side;

import java.awt.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.GlobalSettings;
import mmb.menu.world.inv.InventoryController;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import java.awt.Color;

/**
 *
 * @author oskar
 *
 */
public class ModularChestGUI extends GUITab implements SafeCloseable{
	public ModularChestGUI(WorldWindow window, ModularBlock<?, ?, ?, ?> mblock) {
		this.mblock = mblock;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][grow]"));
		
		invctrl = window.playerInventory();
		add(invctrl, "cell 0 0 1 3,grow");
		
		
		add(mcgL = createSlot(mblock.slot(Side.L)), "cell 1 1,grow");
		add(mcgR = createSlot(mblock.slot(Side.R)), "cell 2 1,grow");
		add(mcgU = createSlot(mblock.slot(Side.U)), "cell 1 0,grow");
		add(mcgD = createSlot(mblock.slot(Side.D)), "cell 1 2,grow");
		add(mcgC = createSlot(mblock.slotC()),      "cell 2 0,grow");
		
		JButton exit = new JButton(GlobalSettings.$res("exit"));
		exit.setBackground(Color.RED);
		exit.addActionListener(e -> window.closeWindow(this));
		add(exit, "cell 2 2,grow");
	}
	
	@Nullable private <Telement extends BlockModuleOrCore<Telement, ?, ?>, Tgui extends Component&SafeCloseable> ModuleOrCoreGUI<Telement, Tgui>
	createSlot(@Nullable Slot<Telement> slot){
		if(slot == null) return null;
				
		ModuleOrCoreGUI<Telement, Tgui> mcg = new ModuleOrCoreGUI<>(slot, invctrl);
		Color c = (slot instanceof CoreSlot)?Color.ORANGE:Color.BLUE;
		mcg.setBorder(new BevelBorder(BevelBorder.LOWERED, c, c));
		return mcg;
	}
	private static final long serialVersionUID = 2850183940656718621L;
	
	private ModuleOrCoreGUI<?, ?> mcgL;
	private ModuleOrCoreGUI<?, ?> mcgR;
	private ModuleOrCoreGUI<?, ?> mcgC;
	private ModuleOrCoreGUI<?, ?> mcgU;
	private ModuleOrCoreGUI<?, ?> mcgD;
	@Nonnull private final ModularBlock<?, ?, ?, ?> mblock;
	@Nonnull private final InventoryController invctrl;

	@Override
	public void close(WorldWindow window) {
		close();
	}

	@Override
	public void close() {
		mblock.closeTab(this);
		if(mcgL != null) mcgL.close();
		if(mcgR != null) mcgR.close();
		if(mcgC != null) mcgC.close();
		if(mcgU != null) mcgU.close();
		if(mcgD != null) mcgD.close();
	}

}
