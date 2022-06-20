/**
 * 
 */
package mmb.MODS.info;

import static mmb.GlobalSettings.$res;

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
	
	public final String title;
	ModState(String title){
		this.title = title;
	}
}
