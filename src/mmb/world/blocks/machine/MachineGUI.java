/**
 * 
 */
package mmb.world.blocks.machine;

import static mmb.GlobalSettings.$res;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import mmb.debug.Debugger;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.machine.SideConfigCtrl;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmb.world.blocks.machine.SkeletalBlockMachine.Update;
import mmb.world.inventory.Inventory;
import net.miginfocom.swing.MigLayout;
import io.github.parubok.text.multiline.MultilineLabel;

class MachineGUI extends GUITab{
	private static final long serialVersionUID = -4029353853474275301L;
	private final SideConfigCtrl sie, soe, sii, soi;
	@Nonnull private InventoryController playerController;
	private final Component main;
	private final List<InventoryController> invctrls = new ArrayList<>();
	private final SkeletalBlockMachine machine;
	
 	void refresh() {
		if(main instanceof Update) ((Update) main).update();
		for(InventoryController invctrl: invctrls) {
			invctrl.refresh();
		}
		//playerController.refresh();
		repaint();
	}
	public MachineGUI(SkeletalBlockMachine machine, WorldWindow window) {
		this.machine = machine;
		playerController = new InventoryController();
		playerController.setInv(window.getPlayer().inv);
		Box box = new Box(BoxLayout.Y_AXIS);
		main = machine.createGUI();
		if((machine.flags & SkeletalBlockMachine.SETTING_FLAG_ELEC_INPUT) != 0) {
			sie = createCtrl(machine.cfgInElec, box, "Electrical inputs");
			ElectricalBatteryVisualiser visual = new ElectricalBatteryVisualiser();
			visual.setBattery(machine.inElec);
			box.add(visual);
		}else sie = null;
		if((machine.flags & SkeletalBlockMachine.SETTING_FLAG_ELEC_OUTPUT) != 0) {
			soe = createCtrl(machine.cfgOutElec, box, "Electrical outputs");
			ElectricalBatteryVisualiser visual = new ElectricalBatteryVisualiser();
			visual.setBattery(machine.outElec);
			box.add(visual);
		}else soe = null;
		if((machine.flags & SkeletalBlockMachine.SETTING_FLAG_ITEM_INPUT) != 0) {
			sii = createCtrlInv(machine.cfgInItems, box, "Item inputs", machine.inItems, false);
		}else sii = null;
		if((machine.flags & SkeletalBlockMachine.SETTING_FLAG_ITEM_OUTPUT) != 0) {
			soi = createCtrlInv(machine.cfgOutItems, box, "Item outputs", machine.outItems, true);
		}else soi = null;
		setLayout(new MigLayout("", "[300.00px][][]", "[277.00px,grow]"));
		
		
		
		JButton exit = new JButton();
		exit.setBackground(Color.red);
		exit.setText($res("exit"));
		exit.addActionListener(e -> window.closeWindow(this));
		
		
		add(playerController, "cell 0 0,grow");
		box.add(exit);
		add(box, "cell 1 0,alignx left,aligny top");
		
		MultilineLabel multilineLabel = new MultilineLabel($res("depr-notranslate"));
		add(multilineLabel, "cell 2 0");
		if(main != null) add(main, BorderLayout.CENTER);
		
		Component inner = machine.createGUI();
		if(inner != null) add(inner, "cell 2 0, grow");
	}
	private static SideConfigCtrl createCtrl(SideConfig cfg, Box box, String s) {
		SideConfigCtrl result = new SideConfigCtrl(cfg);
		box.add(new JLabel(s));
		box.add(result);
		return result;
	}
	private SideConfigCtrl createCtrlInv(SideConfig cfg, Box box, String s, Inventory inv, boolean exit) {
		Box subbox = new Box(BoxLayout.X_AXIS);
		InventoryController invctrl = new InventoryController(inv);
		invctrls.add(invctrl);
		MoveItems move = new MoveItems(playerController, invctrl, exit ? MoveItems.LEFT : MoveItems.BOTH);
		subbox.add(move);
		subbox.add(invctrl);
		SideConfigCtrl result = new SideConfigCtrl(cfg);
		subbox.add(result);
		box.add(new JLabel(s));
		box.add(subbox);
		return result;
	}
	private final Debugger debug = new Debugger("MACHINE GUI");
	@Override
	public void createTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroyTab(WorldWindow window) {
		if(sie != null) sie.close();
		if(soe != null) soe.close();
		if(sii != null) sii.close();
		if(soi != null) soi.close();
		if(main instanceof AutoCloseable)
			try {
				((AutoCloseable) main).close();
			} catch (Exception e) {
				debug.pstm(e, "Failed to close machine GUI");
			}
		machine.gui = null;
	}
}