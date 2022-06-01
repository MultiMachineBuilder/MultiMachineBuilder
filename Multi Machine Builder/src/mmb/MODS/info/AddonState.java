/**
 * 
 */
package mmb.MODS.info;

import static mmb.GlobalSettings.$res;

/**
 * @author oskar
 *
 */
public enum AddonState {
	/** Indicates a non-existent mod file */
	NOEXIST($res("cguims-none")),
	/** Indicates a corrupt mod file */
	BROKEN($res("cguims-bad")),
	/** Indicates a disabled mod, which won't run*/
	DISABLE($res("cguims-dis")),
	/** Indicates an active mod*/
	ENABLE($res("cguims-ena")),
	/** Indicates a mod which failed to load*/
	DEAD($res("cguims-dead")),
	/** Indicates an API package without any mods*/
	API($res("cguims-api")),
	/** Indicates a media package without any code*/
	MEDIA($res("cguims-media")),
	/** Indicates an empty file*/
	EMPTY($res("cguims-empty")),
	/** Indicates a directory */
	DIRECTORY($res("cguims-dir")),
	/** Indicates a mod which must be downloaded, but no network is avaliable */
	CONNECTLOST($res("cguims-nonet")),
	/** Indicates that the file is too big to load */
	BLOATED($res("cguims-bloat"));
	
	public final String title;
	AddonState(String title){
		this.title = title;
	}
}
