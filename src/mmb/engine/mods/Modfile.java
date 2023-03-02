/**
 * 
 */
package mmb.engine.mods;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mmb.engine.files.AdvancedFile;
import mmb.engine.files.OnlineFile;

/**
 * Represents a modfile which may conain mods or other content
 * @author oskar
 */
public class Modfile {
	/** The displayed mod path */
	public final String path;
	/** The type of the modfile */
	public ModfileState state = ModfileState.MEDIA;
	/** The mod file reference */
	public final AdvancedFile file;
	/** Is the modfile valid? */
	public boolean hasValidData = false;
	private final Set<String> classnames0 = new HashSet<>();
	/** List of classes contained in this mod */
	public final Set<String> classnames = Collections.unmodifiableSet(classnames0);
	/** @return is the mod downloaded on load? */
	public boolean isOnline() {
		return file instanceof OnlineFile;
	}
	/**
	 * Creates a modfile
	 * @param path display mod path
	 * @param file mod file reference
	 */
	public Modfile(String path, AdvancedFile file) {
		super();
		this.path = path;
		this.file = file;
	}
	/**
	 * Adds a loaded class to the modfile
	 * @param path1 path to the class file
	 */
	public void addClassName(String path1) {
		String shorter = path1.substring(0, path1.length() - 6);
		shorter = shorter.replace('/', '.');
		shorter = shorter.replace('\\', '.');
		classnames0.add(shorter);
		Mods.classnames.add(shorter);
	}
}
