/**
 * 
 */
package mmb.MODS.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author oskar
 *
 */
public class Mods {
	private Mods() {}
	public static final List<Modfile> files = new ArrayList<>();
	public static final List<ModInfo> mods = new ArrayList<>();
	public static final Set<String> classnames = new HashSet<>();
}
