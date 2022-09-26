/**
 * 
 */
package mmb.MODS.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pploder.events.*;

import mmb.CatchingEvent;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Mods {
	private Mods() {}
	
	private static final Debugger debug = new Debugger("MODS");
	public static final Event<Void> firstRun = new CatchingEvent<>(debug, "Can't run 1st stage for given event");
	public static final Event<Void> makeContent = new CatchingEvent<>(debug, "Can't run 2nd stage for given event");
	public static final Event<Void> integration = new CatchingEvent<>(debug, "Can't run 3rd stage for given event");
	
	public static final List<Modfile> files = new ArrayList<>();
	public static final List<ModInfo> mods = new ArrayList<>();
	public static final Set<String> classnames = new HashSet<>();
}
