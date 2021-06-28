/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import javax.annotation.Nonnull;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public class Pipe extends AbstractBasePipe {
	protected final Pusher invwA, invwB;
	protected Pipe(int x, int y, @Nonnull BlockMap owner2, @Nonnull Side sideA,
			@Nonnull Side sideB, @Nonnull BlockType type, @Nonnull RotatedImageGroup rig) {
		super(x, y, owner2, type, 2, rig);
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
