/**
 * 
 */
package mmb.WORLD.blocks.ipipe;

import static mmb.GlobalSettings.$res;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.ControllableFilter;
import mmb.DATA.variables.Variable;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.machine.FilterGUI;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.inventory.storage.SingleItemInventory;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.filter.ItemFilter;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * Represents a binding pipe. The binding side goes to UP
 */
public class PipeFilter extends AbstractBasePipe implements BlockActivateListener, ControllableFilter{
	protected final @Nonnull Pusher toCommon;
	protected final @Nonnull Pusher toSide;
	protected final @Nonnull Pusher toMain;
	/**
	 * The inventory which houses the item filter
	 */
	public final SingleItemInventory filter = new SingleItemInventory();
	/**
	 * Creates a filtering pipe.
	 * @param type block type
	 * @param binding binding side
	 * @param rig texture
	 */
	public PipeFilter(BlockType type, ChirotatedImageGroup rig) {
		super(type, 3, rig);
		Variable<ItemEntry> varM = getSlot(0);
		Variable<ItemEntry> varS = getSlot(1);
		Variable<ItemEntry> varC = getSlot(2);
		
		toCommon = new Pusher(varC, Side.U);
		toSide = new Pusher(varS, Side.R);
		toMain = new Pusher(varM, Side.D);
		
		InventoryReader readC = new ExtractForItemVar(varC);
		InventoryReader readM = new ExtractForItemVar(varM);
		InventoryReader readS = new ExtractForItemVar(varS);
		
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
	
	private FilterGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		gui = new FilterGUI(this, window);
		window.openAndShowWindow(gui, $res("wgui-filter"));
	}
	@Override
	public SingleItemInventory[] getFilters() {
		return new SingleItemInventory[]{filter};
	}
	@Override
	public String[] getTitles() {
		return null; //NOSONAR the method allows null
	}
	@Override
	public void destroyTab(GUITab filterGUI) {
		if(filterGUI != gui) throw new IllegalStateException("Wrong GUI");
		gui = null;
	}

}
