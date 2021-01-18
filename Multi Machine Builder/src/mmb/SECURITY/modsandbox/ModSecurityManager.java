/**
 * 
 */
package mmb.SECURITY.modsandbox;

import java.io.File;
import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

import mmb.MODS.info.AddonCentral;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ModSecurityManager extends SecurityManager {
	private static Debugger debug = new Debugger("MOD SECURITY");
	@Override
	public void checkPermission(Permission perm) {
		switch(perm.getName()) {
		case "setIO":
			throw new SecurityException("Mods aren't allowed to change critical I/O streams.");
		}
	}
	@SuppressWarnings("unchecked")
	private Class<? extends AddonCentral> getInvolvedMod(){
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for(StackTraceElement ste: stackTrace) {
			Class cls;
			try {
				cls = Class.forName(ste.getClassName());
			} catch (ClassNotFoundException e) {
				return null;
			}
			if(AddonCentral.class.isAssignableFrom(cls)) {
				return cls;
			}
		}
		return null;
	}
	
	
}
