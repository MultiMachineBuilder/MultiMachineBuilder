/**
 * 
 */
package mmb.content.imachine.pipe;

import static mmb.engine.settings.GlobalSettings.$res;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.imachine.filter.ControllableFilter;
import mmb.content.imachine.filter.FilterGUI;
import mmb.content.imachine.filter.ItemFilter;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 * Represents a binding pipe. The binding side goes to UP
 */
public class PipeFilter extends AbstractBasePipe implements BlockActivateListener, ControllableFilter{
	protected final Pusher toCommon;
	protected final Pusher toSide;
	protected final Pusher toMain;
	/**
	 * The inventory which houses the item filter
	 */
	public final SingleItemInventory filter = new SingleItemInventory();
	/**
	 * Creates a filtering pipe.
	 * @param type block type
	 * @param rig texture
	 */
	public PipeFilter(BlockType type, ChirotatedImageGroup rig) {
		super(type, 3, rig);
		SingleItemInventory varM = items[0];
		SingleItemInventory varS = items[1];
		SingleItemInventory varC = items[2];
		
		toCommon = new Pusher(varC, Side.U);
		toSide = new Pusher(varS, Side.R);
		toMain = new Pusher(varM, Side.D);
		
		InventoryReader readC = varC.createReader();
		InventoryReader readM = varM.createReader();
		InventoryReader readS = varS.createReader();
		
		InventoryWriter bottom = new InventoryWriter.Priority(new InventoryWriter.Filtering(toSide, this::test), toMain);
		
		inR = toCommon;
		inD = bottom;
		inU = toCommon;
		
		outR = readS;//side
		outU = readM;//not fit
		outD = readC;//common
	}
	public boolean test(ItemEntry in) {
		ItemEntry item = filter.getContents();
		if(item instanceof ItemFilter)
			return ((ItemFilter) item).test(in);
		return false;
	}
	
	@Override
	public void onTick(MapProxy map) {
		toCommon.push();
		toSide.push();
		toMain.push();
	}
	@Override
	public BlockEntry blockCopy() {
		PipeFilter result = new PipeFilter(type(), getImage());
		System.arraycopy(items, 0, result.items, 0, 3);
		return result;
	}
	
	@Nil private FilterGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		FilterGUI gui0 = new FilterGUI(this, window);
		gui = gui0;
		window.openAndShowWindow(gui0, $res("wgui-filter"));
	}
	@Override
	public SingleItemInventory[] getFilters() {
		return new SingleItemInventory[]{filter};
	}
	@SuppressWarnings("null")
	@Override
	public @NN String @Nil [] getTitles() {
		return null; //NOSONAR the method allows null
	}
	@Override
	public void destroyTab(GUITab filterGUI) {
		if(filterGUI != gui) throw new IllegalStateException("Wrong GUI");
		gui = null;
	}

}
