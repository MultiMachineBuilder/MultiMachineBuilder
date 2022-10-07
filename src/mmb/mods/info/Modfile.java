/**
 * 
 */
package mmb.mods.info;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mmb.files.AdvancedFile;
import mmb.files.OnlineFile;

/**
 * @author oskar
 *
 */
public class Modfile {
	public final String path;
	public ModfileState state = ModfileState.MEDIA;
	public final AdvancedFile file;
	public boolean hasValidData = false;
	private final Set<String> classnames0 = new HashSet<>();
	public final Set<String> classnames = Collections.unmodifiableSet(classnames0);
	public boolean isOnline() {
		return file instanceof OnlineFile;
	}
	public Modfile(String path, AdvancedFile file) {
		super();
		this.path = path;
		this.file = file;
	}
	public void addClassName(String path) {
		int classExtName = 6;
		String shorter = path.substring(0, path.length() - 6);
		shorter = shorter.replace('/', '.');
		shorter = shorter.replace('\\', '.');
		classnames0.add(shorter);
		Mods.classnames.add(shorter);
	}
}
