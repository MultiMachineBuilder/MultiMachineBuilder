/**
 * 
 */
package mmb.MODS.info;

/**
 * @author oskar
 *
 */
public enum AddonState {
	NOEXIST, BROKEN, DISABLE, ENABLE, DEAD, API, MEDIA, EMPTY;
	
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
