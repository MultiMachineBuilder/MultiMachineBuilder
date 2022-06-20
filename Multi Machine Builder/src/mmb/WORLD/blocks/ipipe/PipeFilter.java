/**
 * 
 */
package mmb.WORLD.blocks.ipipe;

import javax.annotation.Nonnull;

import mmb.DATA.variables.Variable;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.inventory.storage.SingleItemInventory;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.filter.ItemFilter;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * Represents a binding pipe. The binding side goes to UP
 */
public class PipeFilter extends AbstractBasePipe{
	@Nonnull private final Side binding;
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
	public PipeFilter(BlockType type, Side binding, ChirotatedImageGroup rig) {
		super(type, 3, rig);
		this.binding = binding;
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
		PipeFilter result = new PipeFilter(type(), binding, getImage());
		System.arraycopy(items, 0, result.items, 0, 3);
		return result;
	}

}
