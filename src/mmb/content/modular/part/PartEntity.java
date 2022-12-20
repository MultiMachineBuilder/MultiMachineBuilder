/**
 * 
 */
package mmb.content.modular.part;

import mmb.Nil;

/**
 *
 * @author oskar
 *
 */
public abstract class PartEntity implements PartEntry {
	protected abstract int hash0();
	protected abstract boolean equal0(PartEntity other);

	@Override
	public final int hashCode() {
		return 31*super.hashCode() + hash0();
	}

	@Override
	public final boolean equals(@Nil Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		PartEntity other = (PartEntity) obj;
		return equal0(other);
	}
}
