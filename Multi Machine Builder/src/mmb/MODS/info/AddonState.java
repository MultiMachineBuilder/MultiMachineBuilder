/**
 * 
 */
package mmb.MODS.info;

/**
 * @author oskar
 *
 */
public enum AddonState {
	/** Indicates a non-existent mod file */
	NOEXIST("Missing files"),
	/** Indicates a corrupt mod file */
	BROKEN("Corrupt"),
	/** Indicates a disabled mod, which won't run*/
	DISABLE("Disabled"),
	/** Indicates an active mod*/
	ENABLE("Operative"),
	/** Indicates a mod which failed to load*/
	DEAD("Crashed"),
	/** Indicates an API package without any mods*/
	API("API"),
	/** Indicates a media package without any code*/
	MEDIA("Media package"),
	/** Indicates an empty file*/
	EMPTY("Empty"),
	/** Indicates a directory */
	DIRECTORY("Directory"),
	/** Indicates a mod which must be downloaded, but no network is avaliable */
	CONNECTLOST("No network connection"),
	/** Indicates that the file is too big to load */
	BLOATED("Too big");
	
	public final String title;
	AddonState(String title){
		this.title = title;
	}
}
