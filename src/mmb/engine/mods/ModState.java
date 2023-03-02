/**
 * 
 */
package mmb.engine.mods;

import static mmb.engine.settings.GlobalSettings.$res;

/**
 * @author oskar
 *
 */
public enum ModState {
	/** Indicates a disabled mod, which won't run*/
	DISABLE($res("cguims-dis")),
	/** Indicates an active mod*/
	ENABLE($res("cguims-ena")),
	/** Indicates a mod which failed to load*/
	DEAD($res("cguims-dead"));
	
	/** Display label of this mod state*/
	public final String title;
	ModState(String title){
		this.title = title;
	}
}
