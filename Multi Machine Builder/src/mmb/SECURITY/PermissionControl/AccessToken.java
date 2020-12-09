/**
 * 
 */
package mmb.SECURITY.PermissionControl;

/**
 * @author oskar
 * The access token is an access permissions provider. It also checks security when constructed
 */
public class AccessToken implements AccessPermissionsControl {
	private final AccessPermissionsControl apc;

	@Override
	public boolean read() {
		return apc.read();
	}

	@Override
	public boolean write() {
		return apc.write();
	}


	@Override
	public boolean delete() {
		return apc.delete();
	}
	
	@Override
	public boolean insert() {
		return apc.insert();
	}

	@Override
	public void setRead(boolean read) {}

	@Override
	public void setWrite(boolean read) {}

	@Override
	public void setDelete(boolean read) {}

	@Override
	public void setInsert(boolean read) {}
	/**
	 * 
	 */
	public AccessToken(Secure lock, Security key, AccessPermissionsControl ctrl) {
		if(!lock.checkSecurity(key)) throw new SecurityException("No grant permissions");
		apc = ctrl;
	}

}
