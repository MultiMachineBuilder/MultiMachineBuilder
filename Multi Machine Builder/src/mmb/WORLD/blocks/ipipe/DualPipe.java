/**
 * 
 */
package mmb.WORLD.blocks.ipipe;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * Represent a pair of pipes.
 * 
 * Bindings: up - side A, left - side B
 */
public class DualPipe extends AbstractBasePipe {

	@Nonnull protected final Pusher invwA, invwB, invwC, invwD;
	/**
	 * 
	 * @param sideA side bound to uppper interface
	 * @param sideB side bound to left interface
	 * @param type block type
	 * @param rig texture
	 */
	public DualPipe(Side sideA, Side sideB,
			BlockType type, ChirotatedImageGroup rig) {
		super(type, 4, rig);
		Variable<ItemEntry> varA = getSlot(0);
		Variable<ItemEntry> varU = getSlot(1);
		Variable<ItemEntry> varB = getSlot(2);
		Variable<ItemEntry> varL = getSlot(3);
		invwA = new Pusher(varA, Side.U);
		invwB = new Pusher(varU, sideA);
		invwC = new Pusher(varB, Side.L);
		invwD = new Pusher(varL, sideB);
		setIn(invwA, sideA);
		setIn(invwB, Side.U);
		setIn(invwC, sideB);
		setIn(invwD, Side.L);
		setOut(new ExtractForItemVar(varA), sideA);
		setOut(new ExtractForItemVar(varU), Side.U);
		setOut(new ExtractForItemVar(varB), sideB);
		setOut(new ExtractForItemVar(varL), Side.L);
	}

	@Override
	public void onTick(MapProxy map) {
		invwA.push();
		invwB.push();
		invwC.push();
		invwD.push();
	}
}
