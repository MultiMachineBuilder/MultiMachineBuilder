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
	/** Component type of this component */
	@NN public final ComponentGenerator gen;
	/** Component tier of this component */
	@NN public final ComponentTier tier;
	ElectronicsComponent(ComponentGenerator gen, ComponentTier tier){
		this.gen = gen;
		this.tier = tier;
	}
}
