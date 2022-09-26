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
 *
 */
public class Pipe extends AbstractBasePipe {
	@Nonnull protected final Pusher invwA, invwB;
	@Nonnull final Side sideA, sideB;
	public Pipe(Side sideA, Side sideB, BlockType type, ChirotatedImageGroup rig) {
		super(type, 2, rig);
		this.sideA = sideA;
		this.sideB = sideB;
		SingleItemInventory varA = items[0];
		SingleItemInventory varB = items[1];
		invwA = new Pusher(varA, sideB);
		invwB = new Pusher(varB, sideA);
		setIn(invwA, sideA);
		setIn(invwB, sideB);
		setOut(varA.createReader(), sideA);
		setOut(varB.createReader(), sideB);
	}

	@Override
	public void onTick(MapProxy map) {
		invwA.push();
		invwB.push();
	}

	@Override
	public BlockEntry blockCopy() {
		Pipe result = new Pipe(sideA, sideB, type(), getImage());
		System.arraycopy(items, 0, result.items, 0, 2);
		return result;
	}
}
