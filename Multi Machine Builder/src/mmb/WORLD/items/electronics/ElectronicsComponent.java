/**
 * 
 */
package mmb.WORLD.items.electronics;

import javax.annotation.Nonnull;

import mmb.WORLD.item.Item;

/**
 * @author oskar
 *
 */
public class ElectronicsComponent extends Item {
	@Nonnull public final ComponentGenerator gen;
	@Nonnull public final ComponentTier tier;
	ElectronicsComponent(ComponentGenerator gen, ComponentTier tier){
		this.gen = gen;
		this.tier = tier;
	}
}
