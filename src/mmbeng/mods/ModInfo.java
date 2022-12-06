/**
 * 
 */
package mmbeng.mods;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class ModInfo {
	@Nonnull public final AddonCentral instance;
	@Nonnull public final ModMetadata meta;
	@Nonnull public ModState state = ModState.ENABLE;
	public ModInfo(AddonCentral instance) {
		this.instance = instance;
		this.meta = instance.info();
	}
}