/**
 * 
 */
package mmb.world.blocks.ipipe;

import javax.annotation.Nonnull;

import mmb.data.variables.Variable;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.items.ItemEntry;
import mmb.world.rotate.ChirotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 * Represents a binding pipe. The binding side goes to UP
 */
public class PipeBinder extends AbstractBasePipe{
	@Nonnull private final Side binding;
	protected final @Nonnull Pusher toCommon, toSide, toMain;
	/**
	 * Creates a binding pipe.
	 * @param type block type
	 * @param binding binding side
	 * @param rig texture
	 */
	public PipeBinder(BlockType type, Side binding, ChirotatedImageGroup rig) {
		super(type, 3, rig);
		this.binding = binding;
		SingleItemInventory varM = items[0];
		SingleItemInventory varS = items[1];
		SingleItemInventory varC = items[2];
		
		toCommon = new Pusher(varC, Side.U);
		toSide = new Pusher(varS, binding);
		toMain = new Pusher(varM, Side.D);
		
		InventoryReader readC = varC.createReader();
		InventoryReader readM = varM.createReader();
		InventoryReader readS = varS.createReader();
		
		InventoryWriter top = new InventoryWriter.Priority(toMain, toSide);
		
		setIn(toCommon, binding);
		inU = top;
		inD = toCommon;
		
		setOut(readS, binding);
		outD = readM;
		outU = readC;
	}
	@Override
	public void onTick(MapProxy map) {
		toCommon.push();
		toSide.push();
		toMain.push();
	}
	@Override
	public BlockEntry blockCopy() {
		PipeBinder result = new PipeBinder(type(), binding, getImage());
		System.arraycopy(items, 0, result.items, 0, 3);
		return result;
	}

}
