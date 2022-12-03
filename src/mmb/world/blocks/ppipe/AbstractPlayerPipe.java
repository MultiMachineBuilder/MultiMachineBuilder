/**
 * 
 */
package mmb.world.blocks.ppipe;

import javax.annotation.Nonnull;

import mmb.debug.Debugger;
import mmb.world.blocks.BlockEntityChirotable;
import mmb.world.rotate.Side;
import mmb.world.rotate.Sided;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 *
 */
public abstract class AbstractPlayerPipe extends BlockEntityChirotable {
	public AbstractPlayerPipe() {
		addChiralityListener(e -> initConnections(posX(), posY()));
		addRotationListener(e -> initConnections(posX(), posY()));
	}
	public final Sided<PipeTunnelEntry> sides = new Sided<>();
	/**
	 * Set the pipe connections for this pipe, and reset any path
	 * @param x X coordinate of the pipe
	 * @param y Y coordinate of the pipe
	 */
	protected abstract void initConnections(int x, int y);
	/**
	 * @author oskar
	 * A helper class to make pipe tunnels
	 */
	protected class TunnelHelper extends PipeTunnel{
		@Nonnull protected final Side from;
		@Nonnull protected final Side to;
		/**
		 * @param from side, from which player enters in FWD direction
		 * @param to side, to which player goes in FWD direction
		 */
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
	public void onStartup(World map, int x, int y) {
		initConnections(x, y);
	}
	@Override
	public void onPlace(World map, int x, int y) {
		initConnections(x, y);
	}
	@Override
	public PipeTunnelEntry getPipeTunnel(Side s) {
		return sides.get(getChirotation().negate().apply(s));
	}
	private static final Debugger debug = new Debugger("PLAYER PIPE");

	
}
