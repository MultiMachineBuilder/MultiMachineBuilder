/**
 * 
 */
package mmb.WORLD.electromachine;

/**
 * @author oskar
 *
 */
public enum CycleResult {
	/** Indicates that cycle did not run recipe and found no items */
	EMPTY,
	/** Indicates that cycle did not start crafting, because only items were incompatible */
	UNSUPPORTED,
	/** Indicates that cycle started and has withdrawn items*/
	WITHDRAW,
	/** Indicates that cycle has finished and inserted items into output inventory */
	OUTPUT,
	/** Indicates that cycle has made progress, but not finished processing */
	RUN,
	/** Indicates that items are part of supported recipe, but only some items exist in the machine*/
	PARTIAL;
}
