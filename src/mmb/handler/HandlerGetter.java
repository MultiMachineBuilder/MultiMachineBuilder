package mmb.handler;

import mmb.engine.block.BlockEntry;

public interface HandlerGetter{
	public Object getHandler(HandlerKey key, BlockEntry block);
}
