/**
 * 
 */
package mmb;

import java.security.BasicPermission;

/**
 * @author oskar
 *
 */
public class PermissionCrashGame extends BasicPermission {
	private static final long serialVersionUID = -7717718203400673098L;
	/**
	 * Construct a permission to crash the game
	 */
	public PermissionCrashGame() {
		super("CrashTheGame");
	}
}
