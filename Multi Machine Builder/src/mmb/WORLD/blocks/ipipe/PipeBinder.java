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
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

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
