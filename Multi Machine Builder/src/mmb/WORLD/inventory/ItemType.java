/**
 * 
 */
package mmb.WORLD.inventory;

/**
 * @author oskar
 *
 */
public interface ItemType extends Item {
	@Override
	default public ItemType getType() {
		return this;
	}
}
