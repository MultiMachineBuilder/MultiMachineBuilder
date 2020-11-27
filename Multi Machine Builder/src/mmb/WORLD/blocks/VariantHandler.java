/**
 * 
 */
package mmb.WORLD.blocks;

/**
 * @author oskar
 *
 */
public interface VariantHandler<T extends Enum<T>> {
	/**
	 * @return block's variant display name
	 */
	public String variantName();
}
