/**
 * 
 */
package mmb.addon.data;

/**
 * @author oskar
 *
 */
public enum AddonState {
	NOEXIST, BROKEN, DISABLE, ENABLE, DEAD, API;
	
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
		default:
			return "Unknown";
		}
	}
}
