/**
 * 
 */
package mmb.WORLD_new.worlds.map;

/**
 * @author oskar
 *
 */
public interface DaemonShutdownListener {
	/**
	 * This method is run when the daemon shuts down.
	 * @param e
	 */
	public void onShutdown(DaemonShutdownEvent e);
}
