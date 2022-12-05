/**
 * 
 */
package mmbgame.ppipe;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public abstract class PipeTunnel {
	/**
	 * The path of this pipe tunnel
	 */
	@Nonnull public final Path path = new Path();
	/**
	 * Forward entry to this pipe tunnel
	 */
	@Nonnull public final PipeTunnelEntry FWD = new PipeTunnelEntry(this, Direction.FWD);
	/**
	 * Backward entry to this pipe tunnel
	 */
	@Nonnull public final PipeTunnelEntry BWD = new PipeTunnelEntry(this, Direction.BWD);
	public abstract PipeTunnelEntry findPrev();
	public abstract PipeTunnelEntry findNext();
	public PipeTunnelEntry findNextOrPrevious(Direction d) {
		return d == Direction.BWD?findPrev():findNext();
	}
}
