/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class Pipe extends AbstractBasePipe {
	protected final Pusher invwA, invwB;
	protected Pipe(Side sideA, Side sideB, BlockType type, RotatedImageGroup rig) {
		super(type, 2, rig);
		Variable<ItemEntry> varA = getSlot(0);
		Variable<ItemEntry> varB = getSlot(1);
		invwA = new Pusher(varA, sideB);
		invwB = new Pusher(varB, sideA);
		setIn(invwA, sideA);
		setIn(invwB, sideB);
		setOut(new ExtractForItemVar(varA), sideA);
		setOut(new ExtractForItemVar(varB), sideB);
	}

	@Override
	public void onTick(MapProxy map) {
		invwA.push();
		invwB.push();
	}
}
