/**
 * 
 */
package mmb;

import java.security.Permission;

/**
 * @author oskar
 *
 */
public class ModSecurityManager extends SecurityManager {

	@Override
	public void checkExit(int arg0) {
		throw new SecurityException("Mods aren't allowed to force crash the game");
	}

	@Override
	public void checkPermission(Permission arg0) {
		if("CrashTheGame".equalsIgnoreCase(arg0.getName()))
			throw new SecurityException("Mods aren't allowed to force crash the game");
	}

}
