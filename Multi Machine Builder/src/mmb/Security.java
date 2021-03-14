/**
 * 
 */
package mmb;

import java.io.File;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class Security extends SecurityManager {
	public static final SecurityManager INSTANCE = new Security();
	public final File root = new File("/");
	public final String rootPath = root.getAbsolutePath();

	@Override
	public void checkWrite(@SuppressWarnings("null") @Nonnull String file) {
		String f = new File(file).getAbsolutePath();
		super.checkWrite(file);
		for(StackTraceElement ste: Thread.currentThread().getStackTrace()) {
			if(ste.getClassName().equals("javax.swing.JFileChooser")) return; //definitely user requested
		}
		if(f.startsWith(rootPath)) {
			//Okay
			return;
		}
		throw new SecurityException("Not allowed to write external file: "+f);
	}
}
