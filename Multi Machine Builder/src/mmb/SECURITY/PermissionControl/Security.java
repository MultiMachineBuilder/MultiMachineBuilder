/**
 * 
 */
package mmb.SECURITY.PermissionControl;

import com.pushtorefresh.javac_warning_annotation.Warning;

/**
 * @author oskar
 * A class used to control security. Please do not expose it in public fields.
 */
public class Security {
	public final Secure holder;

	/**
	 * 
	 * @param holder this object's holder
	 * For internal use only
	 */
	@Warning("For internal use in security-checked objects only")
	@Deprecated
	public Security(Secure holder) {
		super();
		this.holder = holder;
	}

	public AccessToken grant(AccessPermissionsControl apc) {
		return holder.grant(this, apc);
	}

}
