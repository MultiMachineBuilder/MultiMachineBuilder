/**
 * 
 */
package mmb.WORLD_new.worlds.map;

/**
 * @author oskar
 *
 */
public class DaemonShutdownEvent {
	public enum Type{
		WORLD, MAP, WORLDBEHAVIOR, MAPBEHAVIOR
	}
	private Daemon daemon;
}
