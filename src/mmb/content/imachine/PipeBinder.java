/**
 * 
 */
package mmb.content.imachine;

import mmb.NN;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmbbase.data.variables.Variable;

/**
 * @author oskar
 * Represents a binding pipe. The binding side goes to UP
 */
public class PipeBinder extends AbstractBasePipe{
	@NN private final Side binding;
	protected final @NN Pusher toCommon, toSide, toMain;
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
