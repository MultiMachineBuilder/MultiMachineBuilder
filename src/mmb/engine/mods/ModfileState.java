/**
 * 
 */
package mmb.engine.mods;

import static mmb.engine.GlobalSettings.$res;

/**
 * @author oskar
 *
 */
public enum ModfileState {
	/** Indicates a non-existent mod file */
	NOEXIST($res("cguims-none")),
	/** Indicates a corrupt mod file */
	BROKEN($res("cguims-bad")),
	/** Indicates an API package with/without mods*/
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
	BLOATED($res("cguims-bloat")),
	/** Indicates a mod which contains code which failed to load*/
	DEAD($res("cguims-dead"));
	
	public final String title;
	ModfileState(String title){
		this.title = title;
	}
}
