/**
 * 
 */
package mmb.content.electronics;

import mmb.NN;
import mmb.engine.item.Item;

/**
 * @author oskar
 *
 */
public class ElectronicsComponent extends Item {
	@NN public final ComponentGenerator gen;
	@NN public final ComponentTier tier;
	ElectronicsComponent(ComponentGenerator gen, ComponentTier tier){
		this.gen = gen;
		this.tier = tier;
	}
}
