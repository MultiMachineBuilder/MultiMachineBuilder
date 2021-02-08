/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.image.BufferedImage;

import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public interface Placer {
	public BufferedImage getIcon();
	public String getTitle();
	public void place(int x, int y, BlockMap map);
	public void openGUI();
	public void closeGUI();
}
