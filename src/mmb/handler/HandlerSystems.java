package mmb.handler;

import java.util.HashMap;
import java.util.Map;

import mmb.PropertyExtension;
import mmb.engine.block.BlockType;

/**
 * Tracks assignments of handlers to block types.
 */
public class HandlerSystems {
	private HandlerSystems() {}
	
	public static final String ITEM = "item";
	public static final String FLUID = "fluid";
	public static final String SIGNAL = "signal";
	public static final String POWER = "electric";
	
	public static final Map<HandlerAssignment, HandlerGetter> handlerGetters = new HashMap<>();
	
	/**
	 * Assigns a handler-mapper to a block type
	 * @param handlerId unique handler ID
	 * @param mapper handler-mapper for a given block type
	 * @return property extension that assigns handlers to block types
	 * @throws ClassCastException outside this method when used in an object not deriving from {@link BlockType}
	 */
	public static PropertyExtension addHandler(String handlerId, HandlerGetter mapper) {
		return object -> {
			BlockType blockType = (BlockType) object;
			HandlerAssignment assignment = new HandlerAssignment(handlerId, blockType);
			handlerGetters.put(assignment, mapper);
		};
	}
}
