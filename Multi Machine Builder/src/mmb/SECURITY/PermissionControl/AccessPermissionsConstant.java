/**
 * 
 */
package mmb.SECURITY.PermissionControl;

/**
 * @author oskar
 *
 */
public class AccessPermissionsConstant implements AccessPermissionsControl{
	public final boolean read, write, insert, delete;
	/**
	 * 
	 */
	public AccessPermissionsConstant(boolean read, boolean write, boolean delete, boolean insert) {
		this.read = read;
		this.write = write;
		this.insert = insert;
		this.delete = delete;
		
	}
	@Override
	public boolean read() {
		return read;
	}
	@Override
	public boolean write() {
		return write;
	}
	@Override
	public boolean delete() {
		return delete;
	}
	@Override
	public boolean insert() {
		return insert;
	}
	@Override
	public void setRead(boolean read) {}
	@Override
	public void setWrite(boolean read) {}
	@Override
	public void setDelete(boolean read) {}
	@Override
	public void setInsert(boolean read) {}

}
