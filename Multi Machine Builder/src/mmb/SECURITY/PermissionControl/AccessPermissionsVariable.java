/**
 * 
 */
package mmb.SECURITY.PermissionControl;

/**
 * @author oskar
 * This class is a POJO intended to represent access permissions. It is not an access token.
 */
public class AccessPermissionsVariable implements AccessPermissionsControl {
	public boolean read, write, delete, insert;
	/**
	 * 
	 */
	public AccessPermissionsVariable() {}

	public AccessPermissionsVariable(boolean read, boolean write, boolean delete, boolean insert) {
		this.read = read;
		this.write = write;
		this.delete = delete;
		this.insert = insert;
	}
	
	public AccessPermissionsVariable(AccessPermissionsControl apc) {
		read = apc.read();
		write = apc.write();
		delete = apc.delete();
		insert = apc.insert();
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
	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public void setWrite(boolean write) {
		this.write = write;
	}

	@Override
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	@Override
	public void setInsert(boolean insert) {
		this.insert = insert;
	}

}
