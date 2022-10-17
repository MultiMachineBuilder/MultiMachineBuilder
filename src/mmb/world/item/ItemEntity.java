/**
 * 
 */
package mmb.world.item;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.world.items.ItemEntry;

/**
 * @author oskar
 *
 */
public abstract class ItemEntity implements ItemEntry{
	protected abstract int hash0();
	protected abstract boolean equal0(ItemEntity other);
	
	@Override
	public abstract @Nullable JsonNode save(); //NOSONAR undefaulted to force item entities to do their save logic

	@Override
	public final int hashCode() {
		return 31*super.hashCode() + hash0();
	}

	@Override
	public final boolean equals(@Nullable Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		return super.equals(obj);
	}
	
	@Override
	public double volume() {
		return type().volume();
	}
}
