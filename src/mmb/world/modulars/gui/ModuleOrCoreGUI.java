/**
 * 
 */
package mmb.world.modulars.gui;

import java.awt.Color;
import java.awt.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import mmb.GlobalSettings;
import mmb.data.reactive.ListenableProperty;
import mmb.data.reactive.Reactor;
import mmb.debug.Debugger;
import mmb.world.crafting.Craftings;
import mmb.world.crafting.RecipeOutput;
import mmb.world.crafting.SimpleItemList;
import mmb.world.inventory.Inventory;
import mmb.world.item.ItemEntry;
import mmb.world.modulars.BlockModuleOrCore;
import mmb.world.modulars.Slot;
import mmb.menu.components.Placeholder;
import mmb.menu.world.inv.InventoryController;

/**
 * A GUI wrapper with buttons, upgrades
 * @author oskar
 * @param <Telement> type of elements
 * @param <Tgui> type of GUIs
 */
public class ModuleOrCoreGUI<Telement extends BlockModuleOrCore<Telement, ?, ?>, Tgui extends Component&SafeCloseable> extends Box implements AutoCloseable{
	private static final long serialVersionUID = 1044001523317555311L;
	
	@Nonnull private static final Debugger debug = new Debugger("MODULAR GUI");
	
	//Managed resources & components
	@Nonnull private final transient Reactor<@Nullable Telement, @Nullable Tgui> guiReactor;
	
	//GUI definition
	@Nonnull public final Slot<Telement> prop;
	@Nonnull public final InventoryController invctrl;
	@Nonnull private final Placeholder placeholder;
	
	/**
	 * Creates a module GUI
	 * @param prop access slot
	 * @param invctrl inventory controller
	 */
	public ModuleOrCoreGUI(Slot<Telement> prop, InventoryController invctrl) {
		super(BoxLayout.Y_AXIS);
		this.prop = prop;
		this.invctrl = invctrl;
		this.guiReactor = new Reactor<>(prop, this::createGUI);
		placeholder = new Placeholder();
		guiReactor.listenrem(el -> {if(el != null) el.close();});
		placeholder.setProperty(guiReactor);
		add(placeholder);
		
		Box buttonbar = new Box(BoxLayout.X_AXIS);
			JButton btnCancel = new JButton(GlobalSettings.$res("cancel"));
			btnCancel.setBackground(Color.RED);
			btnCancel.addActionListener(e -> cancel());
			buttonbar.add(btnCancel);
			
			JButton btnOK = new JButton(GlobalSettings.$res("ok"));
			btnOK.setBackground(Color.GREEN);
			btnOK.addActionListener(e -> OK());
			buttonbar.add(btnOK);
			
			JButton btnUP = new JButton(GlobalSettings.$res("modblocks-upgrade"));
			btnUP.setBackground(Color.CYAN);
			btnUP.addActionListener(e -> upgrade());
			buttonbar.add(btnUP);
		add(buttonbar);
	}
	private void cancel() {
		prop.set(prop.get());
	}
	private void OK() {
		Telement handle = handleOK();
		if(handle != null) prop.set(handle);
	}
	private void upgrade() {
		OK();
		Telement oldelem = prop.get();
		if(oldelem == null) return;
		Tgui gui = guiReactor.get();
		if(gui == null) return;
		ModuleConfigHandler<Telement, Tgui> mch = (ModuleConfigHandler<Telement, Tgui>) oldelem.mch();
		if(mch == null) return;
		Inventory inv = invctrl.getInv();
		if(inv == null) return;
		
		//Get the upgrade
		ItemEntry newupgrade = invctrl.getSelectedItem();
		ItemEntry oldupgrade = mch.upgrades(oldelem);
		Telement newelem = mch.replaceUpgradesWithinItem(oldelem, newupgrade);
		
		//Transact items
		int transact = Craftings.transact(RecipeOutput.orDefault(newupgrade), inv, RecipeOutput.orDefault(oldupgrade), inv, 1);
		if(transact == 1) prop.set(newelem);
	}
	
	private Telement handleOK() {
		Telement el = prop.get();
		if(el == null) return null;
		Tgui gui = guiReactor.get();
		if(gui == null) return null;
		@SuppressWarnings("unchecked")
		ModuleConfigHandler<Telement, Tgui> mch = (ModuleConfigHandler<Telement, Tgui>) el.mch();
		if(mch == null) return null;
		return mch.elementCreate(gui, el, debug, "Failed to create a new element");
	}
	
	private @Nullable Tgui createGUI(@Nullable Telement elem) {
		if(elem == null) return null;
		@SuppressWarnings("unchecked")
		ModuleConfigHandler<Telement, Tgui> mch = (ModuleConfigHandler<Telement, Tgui>) elem.mch();
		if(mch == null) return null;
		Tgui gui = mch.newComponent(invctrl);
		mch.loadGUI(elem, gui);
		return gui;
	}

	@Override
	public void close() {
		guiReactor.close();
	}

}
