package mmb.handler;

import java.util.Objects;

import mmb.engine.block.BlockType;

public record HandlerAssignment(String handlerType, BlockType block) {
	public HandlerAssignment{
		Objects.requireNonNull(handlerType, "handlerType is null");
		Objects.requireNonNull(block, "block is null");
	}
}
