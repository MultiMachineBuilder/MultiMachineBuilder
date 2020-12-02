/**
 * 
 */
package mmb.SECURITY.permissions;

import java.security.BasicPermission;
import java.security.Permission;

import mmb.WORLD.tileworld.world.BlockProxy;

/**
 * @author oskar
 *
 */
public class PermissionReadBlocks extends BasicPermission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5248963237974039170L;
	public BlockProxy proxy;
	
	/**
	 * @param arg0
	 */
	public PermissionReadBlocks(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean implies(Permission p) {
		// TODO Auto-generated method stub
		return super.implies(p);
	}
	


	

}
