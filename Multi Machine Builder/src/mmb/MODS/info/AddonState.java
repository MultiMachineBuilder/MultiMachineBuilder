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
	NOEXIST,
	/** Indicates a corrupt mod file */
	BROKEN,
	/** Indicates a disabled mod, which won't run*/
	DISABLE,
	/** Indicates an active mod*/
	ENABLE,
	/** Indicates a mod which failed to load*/
	DEAD,
	/** Indicates an API package without any mods*/
	API,
	/** Indicates a media package without any code*/
	MEDIA,
	/** Indicates an empty file*/
	EMPTY;
	
	@Override
	public String toString() {
		switch(this) {
		case BROKEN:
			return "Corrupt";
		case DEAD:
			return "Crashed";
		case DISABLE:
			return "Disabled";
		case ENABLE:
			return "Operative";
		case NOEXIST:
			return "Missing files";
		case API:
			return "API";
		case MEDIA:
			return "Media package";
		case EMPTY:
			return "Empty";
		default:
			return "Unknown";
		}
	}
}
