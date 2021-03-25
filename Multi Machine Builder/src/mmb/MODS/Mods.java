/**
 * 
 */
package mmb.MODS;

import com.pploder.events.*;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Mods {
	private Mods() {}
	
	private final static Debugger debug = new Debugger("MODS");
	public static final Event<Void> firstRun = new CatchingEvent<>(debug, "Can't run 1st stage for given event");
	public static final Event<Void> makeContent = new CatchingEvent<>(debug, "Can't run 2nd stage for given event");
	public static final Event<Void> integration = new CatchingEvent<>(debug, "Can't run 3rd stage for given event");
}
