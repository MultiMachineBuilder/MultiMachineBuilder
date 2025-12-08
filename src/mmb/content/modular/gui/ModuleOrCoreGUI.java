/**
 * 
 */
package mmb.content.modular.gui;

import java.awt.Component;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.modular.BlockModuleOrCore;
import mmb.content.modular.Slot;
import mmb.data.reactive.Reactor;
import mmb.menu.components.Placeholder;
import mmb.menu.world.inv.InventoryController;

/**
 * A GUI wrapper with buttons, upgrades
 * @author oskar
 * @param <Telement> type of elements
 * @param <Tgui> type of GUIs
 */
public class ModuleOrCoreGUI<Telement extends BlockModuleOrCore<Telement, ?, ?>, Tgui extends Component&SafeCloseable> extends Placeholder implements AutoCloseable{
	private static final long serialVersionUID = 1044001523317555311L;
	
	//Managed resources & components
	@NN private final transient Reactor<@Nil Telement, @Nil Tgui> guiReactor;
	
	//GUI definition
	@NN public final Slot<Telement> prop;
	@NN public final InventoryController invctrl;
	
	/**
	 * Creates a module GUI
	 * @param prop access slot
	 * @param invctrl inventory controller
	 */
	public ModuleOrCoreGUI(Slot<Telement> prop, InventoryController invctrl) {
		this.prop = prop;
		this.invctrl = invctrl;
		this.guiReactor = new Reactor<>(prop, this::createGUI);
		guiReactor.listenrem(el -> {if(el != null) el.close();});
		setProperty(guiReactor);
	}
	
	private @Nil Tgui createGUI(@Nil Telement elem) {
		if(elem == null) return null;
		@SuppressWarnings("unchecked")
		ModuleConfigHandler<Telement, Tgui> mch = (ModuleConfigHandler<Telement, Tgui>) elem.mch();
		if(mch == null) return null;
		Tgui gui = mch.newComponent(invctrl, elem);
		return gui;
	}

	@Override
	public void close() {
		guiReactor.close();
	}

}
