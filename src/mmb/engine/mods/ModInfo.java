/**
 * 
 */
package mmb.engine.mods;

import mmb.annotations.NN;

/**
 * Contains information about a mod
 * @author oskar
 */
public class ModInfo {
	/** The addon central, if the mod is working */
	@NN public final AddonCentral instance;
	/** The mod information created by the mod */
	@NN public final ModMetadata meta;
	/** Current state of the mod */
	@NN public ModState state = ModState.ENABLE;
	/**
	 * Creates a mod
	 * @param instance addon central
	 */
	public ModInfo(AddonCentral instance) {
		this.instance = instance;
		this.meta = instance.info();
	}
}
