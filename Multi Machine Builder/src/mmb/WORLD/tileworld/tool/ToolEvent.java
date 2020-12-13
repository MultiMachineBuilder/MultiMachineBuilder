/**
 * 
 */
package mmb.WORLD.tileworld.tool;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.joml.Vector2d;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.player.BlockIcon;
import mmb.WORLD.player.DataLayerPlayer;
import mmb.WORLD.tileworld.TileGUI;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.world.BlockProxy;

/**
 * @author oskar
 *
 */
public class ToolEvent {
	/**
	 * Block position for block applications
	 */
	public Point blockPosition;
	
	/**
	 * Vector position for precision applications
	 */
	public Vector2d worldPosition;
	
	/**
	 * Mouse pointer position on the window
	 */
	public Point screenPosition;
	
	/**
	 * Block proxy for making changes to the world.
	 */
	public BlockProxy proxy;
	
	public Block selectedBlock;
	
	public ToolProxy tproxy;
	
	public MouseEvent mouse;

	public BlockIcon selectedBlockIcon;

	public boolean creative;

	public Inventory inventory;

	public DataLayerPlayer player;
	
	public Graphics g;
	
	public TileGUI gui;
	
	/**
	 * 
	 */
	public ToolEvent() {
		// TODO Auto-generated constructor stub
	}

}
