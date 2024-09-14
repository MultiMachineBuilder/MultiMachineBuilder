/**
 * 
 */
package mmb.engine.item;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.Nil;

/**
 * An item, whcih holds additional information
 * @author oskar
 */
public abstract class ItemEntity implements ItemEntry{	
	protected abstract int hash0();
	protected abstract boolean equal0(ItemEntity other);
	@Override
	public final int hashCode() {
		return hash0();
	}
	@Override
	public final boolean equals(@Nil Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		return equal0((ItemEntity) obj);
	}
	
	@Override public abstract @Nil JsonNode save(); //NOSONAR undefaulted to force item entities to do their save logic
	
	@Override
	public double volume() {
		return type().volume();
	}
}
