/**
 * 
 */
package mmb.engine.mods;

import mmb.NN;

/**
 * @author oskar
 *
 */
public class ModInfo {
	@NN public final AddonCentral instance;
	@NN public final ModMetadata meta;
	@NN public ModState state = ModState.ENABLE;
	public ModInfo(AddonCentral instance) {
		this.instance = instance;
		this.meta = instance.info();
	}
}
