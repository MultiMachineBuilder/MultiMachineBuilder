/**
 * 
 */
package mmb.SECURITY.PermissionControl;

/**
 * @author oskar
 *
 */
public interface AccessPermissionsControl {
	//Constants
	public static final AccessPermissionsConstant READ = new AccessPermissionsConstant(true, false, false, false);
	public static final AccessPermissionsConstant WRITE = new AccessPermissionsConstant(false, true, false, false);
	public static final AccessPermissionsConstant RW = new AccessPermissionsConstant(true, true, false, false);
	public static final AccessPermissionsConstant INSERT = new AccessPermissionsConstant(false, false, false, true);
	public static final AccessPermissionsConstant DELETE = new AccessPermissionsConstant(false, false, true, false);
	public static final AccessPermissionsConstant ALL = new AccessPermissionsConstant(true, true, true, true);
	
	public boolean read();
	public boolean write();
	public boolean delete();
	public boolean insert();
	public void setRead(boolean read);
	public void setWrite(boolean write);
	public void setDelete(boolean delete);
	public void setInsert(boolean insert);
	
	default public AccessPermissionsConstant toConstant() {
		return new AccessPermissionsConstant(read(), write(), delete(), insert());
	}
	default public AccessPermissionsConstant toVariable() {
		return new AccessPermissionsConstant(read(), write(), delete(), insert());
	}
	default public void setTo(AccessPermissionsControl apc) {
		setRead(apc.read());
		setWrite(apc.write());
		setDelete(apc.delete());
		setInsert(apc.insert());
	}
}
