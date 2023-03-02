/**
 * 
 */
package mmb.engine.mods;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * List of loaded mods
 * @author oskar
 *
 */
public class Mods {
	private Mods() {}
	/** All found modfiles */
	public static final List<Modfile> files = new ArrayList<>();
	/** All found mods */
	public static final List<ModInfo> mods = new ArrayList<>();
	/** All found mod classes */
	public static final Set<String> classnames = new HashSet<>();
}
