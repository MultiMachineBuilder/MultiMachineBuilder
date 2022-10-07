/**
 * 
 */
package mmb.world.item;

/**
 * @author oskar
 *
 */
public abstract class ItemEntityMutable extends ItemEntity{
	protected ItemEntityMutable(ItemEntityType type) {
		super(type);
	}
	@Override
	protected int hash0() {
		return hashModifier;
	}
	@Override
	protected boolean equal0(ItemEntity other) {
		return false;
	}
	private static int hashes = 0;
	private final int hashModifier = hashes++;
}
