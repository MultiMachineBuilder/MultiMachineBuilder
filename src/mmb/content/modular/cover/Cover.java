package mmb.content.modular.cover;

import mmb.engine.item.ItemEntry;

/**
 * A block cover. It applies 
 */
public interface Cover extends ItemEntry{
	public Object getHandler(String handler, Object innerHandler);
}
