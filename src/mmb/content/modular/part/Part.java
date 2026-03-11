/**
 * 
 */
package mmb.content.modular.part;

import mmb.PropertyExtension;
import mmb.annotations.NN;

/**
 *
 * @author oskar
 *
 */
public class Part extends PartType implements PartEntry{
	/**
	 * Creates a new simple, non-configurable part
	 * @param id
	 * @param properties
	 */
	public Part(String id, PropertyExtension... properties) {
		super(id, properties);
		setPartFactory((json, type) -> this);
	}

	@Override
	public Part itemType() {
		return this;
	}

	@Override
	public @NN PartEntry partClone() {
		return this;
	}
	
}
