/**
 * 
 */
package mmb.WORLD.worlds.player;

import mmb.WORLD.worlds.world.Player;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public interface PlayerPhysics {
	/**
	 * Update the player's speed and position
	 * @param w
	 * @param p
	 * @param ctrlX
	 * @param ctrlY
	 */
	public void onTick(World w, Player p, int ctrlX, int ctrlY);
	/**
	 * @return the description of the current physics state
	 */
	public String description();
}
