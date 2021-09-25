/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * Represents a binding pipe. The binding side goes to UP
 */
public class PipeBinder extends AbstractBasePipe{
	
	protected final @Nonnull Pusher toCommon, toSide, toMain;
	/**
	 * Creates a binding pipe.
	 * @param type block type
	 * @param binding binding side
	 * @param rig texture
	 */
	public PipeBinder(BlockType type, Side binding, RotatedImageGroup rig) {
		super(type, 3, rig);
		Variable<ItemEntry> varM = getSlot(0);
		Variable<ItemEntry> varS = getSlot(1);
		Variable<ItemEntry> varC = getSlot(2);
		
		toCommon = new Pusher(varC, Side.U);
		toSide = new Pusher(varS, binding);
		toMain = new Pusher(varM, Side.D);
		
		InventoryReader readC = new ExtractForItemVar(varC);
		InventoryReader readM = new ExtractForItemVar(varM);
		InventoryReader readS = new ExtractForItemVar(varS);
		
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

}
