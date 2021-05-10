/**
 * 
 */
package mmb.WORLD.item;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import mmb.WORLD.gui.window.WorldFrame;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.player.Player;

/**
 * @author oskar
 *
 */
public interface ItemBehavior {
	/**
	 * This method is run for every in-game tick
	 */
	public void update();
	/**
	 * This method handle mouse press events
	 * @param e event
	 */
	public void mousePress(MouseEvent e);
	/**
	 * This method handle mouse release events
	 * @param e event
	 */
	public void mouseRelease(MouseEvent e);
	/**
	 * This method handle mouse move events
	 * @param e event
	 */
	public void mouseMoved(MouseEvent e);
	/**
	 * This method handle mouse dragged events
	 * @param e event
	 */
	public void mouseDrag(MouseEvent e);
	/**
	 * This method handles key presses
	 * @param e key event
	 * @return if event was handled
	 */
	public boolean keyPress(KeyEvent e);
	
	/**
	 * This method is run when the item behavior is switched to other item.
	 */
	public void disable();
	
	/**
	 * This emthod is run when item behavior is shifted to current item
	 * @param ent item entry
	 * @param p player
	 * <br>Note: If game is played as LSP, the player variable is null
	 * @param frame world frame component.
	 * <br>Note: If item runs on a server, the frame variable is null
	 */
	public void enable(ItemStack ent, Player p, WorldFrame frame);
}
