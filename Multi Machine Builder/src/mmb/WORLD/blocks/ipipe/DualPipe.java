/**
 * 
 */
package mmb.WORLD.blocks.ipipe;

import javax.annotation.Nonnull;

import mmb.DATA.variables.Variable;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.storage.SingleItemInventory;
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

	@Nonnull private final Side sideA, sideB;
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
		this.sideA = sideA;
		this.sideB = sideB;
		SingleItemInventory varA = items[0];
		SingleItemInventory varU = items[1];
		SingleItemInventory varB = items[2];
		SingleItemInventory varL = items[3];
		invwA = new Pusher(varA, Side.U);
		invwB = new Pusher(varU, sideA);
		invwC = new Pusher(varB, Side.L);
		invwD = new Pusher(varL, sideB);
		setIn(invwA, sideA);
		setIn(invwB, Side.U);
		setIn(invwC, sideB);
		setIn(invwD, Side.L);
		setOut(varA.createReader(), sideA);
		setOut(varU.createReader(), Side.U);
		setOut(varB.createReader(), sideB);
		setOut(varL.createReader(), Side.L);
	}

	

	@Override
	public void onTick(MapProxy map) {
		invwA.push();
		invwB.push();
		invwC.push();
		invwD.push();
	}

	@Override
	public BlockEntry blockCopy() {
		DualPipe result = new DualPipe(sideA, sideB, type(), getImage());
		System.arraycopy(items, 0, result.items, 0, 4);
		return result;
	}
}
