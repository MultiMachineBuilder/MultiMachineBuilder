package mmb.content.imachine.tipd;

import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.databind.IntVarSpinnerModel;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.settings.GlobalSettings;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class TipdGUI extends GUITab {
	private static final long serialVersionUID = 1906409333066286689L;
	private final transient TIPD tipd;
	private final IntVarSpinnerModel ivsmPeriod;
	private final IntVarSpinnerModel ivsmMargin;
	private final IntVarSpinnerModel ivsmMaxPack;
	
	TipdGUI(TIPD tipd, WorldWindow window){
		this.tipd = tipd;
		setLayout(new MigLayout("", "[grow,fill][100px][grow]", "[grow][100px][][][center]"));
		
		InventoryController invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "cell 0 0 1 2,grow");
		
		InventoryController invTIPD = new InventoryController(tipd.inv);
		invTIPD.setTitle(GlobalSettings.$res("TIPD"));
		add(invTIPD, "cell 2 0,grow");
		
		SingleInventoryController invBOM = new SingleInventoryController(tipd.bom);
		invBOM.setTitle(GlobalSettings.$res("BOM"));
		add(invBOM, "cell 2 1,grow");
		
		MoveItems moveTIPD = new MoveItems(invPlayer, invTIPD);
		add(moveTIPD, "cell 1 0,grow");
		
		MoveItems moveBOM = new MoveItems(invPlayer, invBOM);
		add(moveBOM, "cell 1 1,grow");
		
		JLabel lblIngredientMargin = new JLabel(GlobalSettings.$res("imargin"));
		add(lblIngredientMargin, "cell 0 2");
		
		JSpinner spinnerMargin = new JSpinner();
		ivsmMargin = new IntVarSpinnerModel(tipd.margin);
		spinnerMargin.setModel(ivsmMargin);
		add(spinnerMargin, "cell 1 2,grow");
		
		JLabel lblMaxPacketsPer = new JLabel(GlobalSettings.$res("maxpackets"));
		add(lblMaxPacketsPer, "cell 0 3");
		
		JSpinner spinnerPacket = new JSpinner();
		ivsmMaxPack = new IntVarSpinnerModel(tipd.maxtransfer);
		spinnerPacket.setModel(ivsmMaxPack);
		add(spinnerPacket, "cell 1 3,grow");
		
		JLabel lblTimeInterval = new JLabel(GlobalSettings.$res("interval"));
		add(lblTimeInterval, "cell 0 4");
		
		JSpinner spinnerInterval = new JSpinner();
		ivsmPeriod = new IntVarSpinnerModel(tipd.interval);
		spinnerInterval.setModel(ivsmPeriod);
		add(spinnerInterval, "cell 1 4,grow");
		
		JButton exit = new JButton(GlobalSettings.$res("exit"));
		exit.setBackground(Color.RED);
		exit.addActionListener(e -> window.closeWindow(this));
		moveTIPD.add(exit);
		
	}
	
	@Override
	public void close(WorldWindow window1) {
		ivsmMaxPack.close();
		ivsmMargin.close();
		ivsmPeriod.close();
		tipd.gui = null;
	}

}
