/**
 * 
 */
package mmb.content.imachine.pipe;

import mmb.NN;
import mmb.content.imachine.pipe.AbstractBasePipe.Pusher;
import mmb.data.variables.Variable;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class Pipe extends AbstractBasePipe {
	@NN protected final Pusher invwA, invwB;
	@NN final Side sideA, sideB;
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
