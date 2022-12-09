/**
 * 
 */
package mmbgame.ppipe;

import javax.annotation.Nonnull;

/**
 * @author oskar
 * Combines a pipe and its entry directionality
 */
public class PipeTunnelEntry {
	/**
	 * A pipe to enter
	 */
	@Nonnull public final PipeTunnel pipe;
	/**
	 * A directionality required to enter the pipe
	 */
	@Nonnull public final Direction dir;
	/**
	 * @param pipe the pipe
	 * @param dir pipe's entry directionality
	 */
	public PipeTunnelEntry(PipeTunnel pipe, Direction dir) {
		super();
		this.pipe = pipe;
		this.dir = dir;
	}
}
