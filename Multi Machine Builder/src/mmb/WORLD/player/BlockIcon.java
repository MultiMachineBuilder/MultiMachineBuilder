/**
 * 
 */
package mmb.WORLD.player;

import java.awt.Graphics;
import java.awt.Point;
import mmb.WORLD.inventory.CreativeWithdrawalInventory;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.world.BlockProxy;

/**
 * @author oskar
 *
 */
public class BlockIcon {
	/**
	 * @return
	 * @see mmb.WORLD.tileworld.block.Block#getTexture()
	 */
	public BlockDrawer getTexture() {
		return block.getTexture();
	}

	/**
	 * @return
	 * @see mmb.WORLD.tileworld.block.Block#getVolume()
	 */
	public double getVolume() {
		return block.getVolume();
	}

	/**
	 * @return
	 * @see mmb.WORLD.tileworld.block.Block#getName()
	 */
	public String getName() {
		return block.getName();
	}

	public Block block;
	public Inventory inv = new CreativeWithdrawalInventory();;
	public BlockProxy proxy;
	public BlockIcon(Block block, BlockProxy proxy) {
		super();
		this.block = block;
	}

	public BlockIcon(Block block, Inventory inv, BlockProxy proxy) {
		super();
		this.block = block;
		this.inv = inv;
		this.proxy = proxy;
	}
	public void placeAt(Point p) {
		if(inv.withdraw(block)) {
			proxy.set(block.newBlock(), p);
		}
	}
	public void placeAt(Point p, BlockProxy proxy) {
		if(inv.withdraw(block)) {
			proxy.set(block.newBlock(), p);
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param g
	 * @see mmb.WORLD.tileworld.block.Block#draw(int, int, java.awt.Graphics)
	 */
	public void draw(int x, int y, Graphics g) {
		block.draw(x, y, g);
	}

	/**
	 * @param p
	 * @param g
	 * @see mmb.WORLD.tileworld.block.Block#draw(java.awt.Point, java.awt.Graphics)
	 */
	public void draw(Point p, Graphics g) {
		block.draw(p, g);
	}
}
