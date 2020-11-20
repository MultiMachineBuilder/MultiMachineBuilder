/**
 * 
 */
package mmb.WORLD.tileworld.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import mmb.DATA.save.DataLayer;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.MapEntry;

/**
 * @author oskar
 *
 */
public class World {
	/**
	 * Contains a tile map with the world.
	 */
	public TileMap blocks;
	
	/**
	 * Contains a list with custom data layers.
	 */
	public List<DataLayer> data = new ArrayList<DataLayer>();
	
	public World(TileMap blocks, List<DataLayer> data) {
		super();
		this.blocks = blocks;
		this.data = data;
	}

	/**
	 * @param newMap
	 */
	public World(TileMap newMap) {
		this.blocks = newMap;
	}

	/**
	 * @param x
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#checkx(int)
	 */
	public boolean checkx(int x) {
		return blocks.checkx(x);
	}

	/**
	 * @param y
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#checky(int)
	 */
	public boolean checky(int y) {
		return blocks.checky(y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#check(int, int)
	 */
	public boolean check(int x, int y) {
		return blocks.check(x, y);
	}

	/**
	 * @param p
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#check(java.awt.Point)
	 */
	public boolean check(Point p) {
		return blocks.check(p);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return blocks.equals(obj);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#getAtPos(int, int)
	 */
	public Block get2(int x, int y) {
		return blocks.get2(x, y);
	}
	
	public MapEntry get(int x, int y) {
		return blocks.get(x, y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#getAbsolute(int, int)
	 */
	public Block getAbsolute(int x, int y) {
		return blocks.getAbsolute(x, y).getType();
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#indexAtPos(int, int)
	 */
	public int indexAtPos(int x, int y) {
		return blocks.indexAtPos(x, y);
	}

	/**
	 * @param p
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#indexAtPos(java.awt.Point)
	 */
	public int indexAtPos(Point p) {
		return blocks.indexAtPos(p);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#indexAbsolute(int, int)
	 */
	public int indexAbsolute(int x, int y) {
		return blocks.indexAbsolute(x, y);
	}

	/**
	 * @param p
	 * @return
	 * @see mmb.WORLD.tileworld.map.TileMap#indexAbsolute(java.awt.Point)
	 */
	public int indexAbsolute(Point p) {
		return blocks.indexAbsolute(p);
	}

}
