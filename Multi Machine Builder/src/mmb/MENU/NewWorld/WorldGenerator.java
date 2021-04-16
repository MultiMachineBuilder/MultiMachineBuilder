/**
 * 
 */
package mmb.MENU.NewWorld;

import java.awt.Component;

import mmb.WORLD.worlds.universe.Universe;

/**
 * @author oskar
 *
 */
public interface WorldGenerator {
	public Component getGUI();
	public void generate(Universe universe);
}
