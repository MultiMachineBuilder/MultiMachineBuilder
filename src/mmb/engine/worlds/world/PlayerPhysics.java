/**
 * 
 */
package mmb.engine.worlds.world;

/**
 * Defines how the player behaves to physical events
 * @author oskar
 */
public interface PlayerPhysics {
	/**
	 * Update the player's speed and position
	 * @param w
	 * @param p
	 * @param ctrlR
	 * @param ctrlD
	 */
	public void onTick(World w, Player p, double ctrlR, double ctrlD);
	/**
	 * @return the description of the current physics state
	 */
	public String description();
}
