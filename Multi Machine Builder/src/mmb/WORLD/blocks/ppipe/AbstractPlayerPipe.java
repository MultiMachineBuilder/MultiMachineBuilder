/**
 * 
 */
package mmb.WORLD.blocks.ppipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.GameObject;
import mmb.WORLD.block.BlockEntityChirotable;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.rotate.Sided;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public abstract class AbstractPlayerPipe extends BlockEntityChirotable {
	public AbstractPlayerPipe() {
		addChiralityListener(e -> initConnections());
		addRotationListener(e -> initConnections());
	}
	public final Sided<PipeTunnelEntry> sides = new Sided<>();
	/**
	 * Set the pipe connections for this pipe, and reset any path
	 */
	protected abstract void initConnections();
	/**
	 * @author oskar
	 * A helper class to make pipe tunnels
	 */
	protected class TunnelHelper extends PipeTunnel{
		@Nonnull protected final Side from;
		@Nonnull protected final Side to;
		public TunnelHelper(Side from, Side to) {
			super();
			this.from = from;
			this.to = to;
		}
		@Override
		public PipeTunnelEntry findPrev() {
			Side s = getChirotation().apply(from);
			return owner().getAtSide(s, posX(), posY()).getPipeTunnel(s.negate());
		}
		@Override
		public PipeTunnelEntry findNext() {
			Side s = getChirotation().apply(to);
			return owner().getAtSide(s, posX(), posY()).getPipeTunnel(s.negate());
		}
	}
	@Override
	public void onStartup(World map) {
		initConnections();
	}
	@Override
	public void onPlace(World map, @Nullable GameObject obj) {
		initConnections();
	}
	@Override
	public PipeTunnelEntry getPipeTunnel(Side s) {
		return sides.get(getChirotation().negate().apply(s));
	}
	private static final Debugger debug = new Debugger("PLAYER PIPE");

	
}
