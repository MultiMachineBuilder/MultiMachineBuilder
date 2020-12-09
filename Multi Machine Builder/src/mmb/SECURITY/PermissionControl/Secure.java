/**
 * 
 */
package mmb.SECURITY.PermissionControl;

/**
 * @author oskar
 *
 */
public interface Secure {
	/**
	 * DO NOT EXPOSE THE SECURITY OBJECT.
	 * Checks a security object without exposing it.
	 * @param s security object to test
	 * @return check
	 */
	public boolean checkSecurity(Security s);
	
	public AccessToken grant(Security s, AccessPermissionsControl apc);
	public AccessToken grant(AccessPermissionsControl apc);
}
