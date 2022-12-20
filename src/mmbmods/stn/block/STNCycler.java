/**
 * 
 */
package mmbmods.stn.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.imachine.SpeedUpgrade;
import mmb.engine.block.BlockEntry;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.data.variables.DataValue;
import mmbbase.data.variables.ListenableValue;
import mmbbase.data.variables.ListenerBooleanVariable;
import mmbbase.data.variables.Variable;
import mmbbase.menu.world.window.WorldWindow;
import mmbmods.stn.network.STNNetworkProcessing.STNRGroupTag;

/**
 * @author oskar
 *
 */
public abstract class STNCycler extends STNBaseMachine implements BlockActivateListener{

	/** The selection */
	@NN
	public final ListenableValue<@Nil ItemEntry> selection = new ListenableValue<>(null);
	/** The speed upgrade slot */
	@NN
	public final SingleItemInventory speedupgrade = new SingleItemInventory();
	/** Should the pusher be signal controlled */
	@NN
	public final ListenerBooleanVariable isControlled = new ListenerBooleanVariable();
	protected double counter = 0;
	protected STNPusherGUI gui;

	@Override
	public STNRGroupTag recipes() {
		return null;
	}

	@Override
	public Inventory storage() {
		return null;
	}

	@Override
	public STNRGroupTag oldrecipes() {
		return null;
	}

	@Override
	public Inventory oldstorage() {
		return null;
	}

	protected STNPusherGUI newGUI(WorldWindow window) {
		return new STNPusherGUI(this, window);
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(gui != null) return;
		if(window == null) return;
		gui = newGUI(window);
		window.openAndShowWindow(gui, title());
	}

	@Override
	public void onTick(MapProxy map) {
		BlockEntry blockTo = getAtSide(getRotation().U());
		if(isControlled.getValue() && !blockTo.provideSignal(getRotation().D())) return;
		counter += SpeedUpgrade.speedup(speedupgrade)/50;
		ItemEntry item = selection.get();
		InventoryWriter writer = blockTo.getInput(getRotation().D());
		InventoryReader reader = blockTo.getOutput(getRotation().D());
		while(counter >= 1) {
			counter--;
			runCycle(item, writer, reader);
		}
	}
	
	@Override
	protected STNCycler blockCopy0() {
		STNCycler copy = copy1();
		copy.selection.set(selection.get());
		copy.counter = counter;
		copy.speedupgrade.setContents(speedupgrade.getContents());
		return copy;
	}
	
	protected abstract STNCycler copy1();
	
	protected abstract String title();

	protected abstract void runCycle(@Nil ItemEntry item, InventoryWriter writer, InventoryReader reader);

	@Override
	protected void save1(ObjectNode node) {
		node.set("selection", ItemEntry.saveItem(selection.get()));
		node.set("upgrade", ItemEntry.saveItem(speedupgrade.getContents()));
		node.put("control", isControlled.getValue());
	}

	@Override
	protected void load1(ObjectNode node) {
		selection.set(ItemEntry.loadFromJson(node.get("selection")));
		speedupgrade.setContents(ItemEntry.loadFromJson(node.get("upgrade")));
		
		JsonNode ctrl = node.get("control");
		boolean result = false;
		if(ctrl != null) result = ctrl.asBoolean();
		isControlled.setValue(result);
	}

}