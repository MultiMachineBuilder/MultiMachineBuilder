package mmb.covers;

import mmb.engine.item.ItemEntry;
import mmb.handler.HandlerKey;

/**
 * A block cover. It applies transformations to handlers, like filtering, flow-rate limiting and monitoring.
 */
public interface Cover extends ItemEntry{
	/**
	 * Wraps an external handler for view of the requesting block
	 * @param handler handler ID
	 * @param key block position and side
	 * @param innerHandler handler to wrap
	 * @return a wrapped handler or {@code null}
	 */
	public Object getHandlerFromBlock(String handler, HandlerKey key, Object innerHandler);
	/**
	 * Wraps a handler for view of outside blocks
	 * @param handler handler ID
	 * @param key block position and side
	 * @param innerHandler handler to wrap
	 * @return a wrapped handler or {@code null}
	 */
	public Object getHandlerToBlock(String handler, HandlerKey key, Object innerHandler);
}
