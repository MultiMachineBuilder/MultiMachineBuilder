/**
 * 
 */
package mmb.world.blocks.ipipe;

import javax.annotation.Nonnull;

import mmb.data.variables.Variable;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.items.ItemEntry;
import mmb.world.rotate.ChirotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

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