/**
 * 
 */
package mmb.tileworld;

import java.awt.Point;

/**
 * @author oskar
 *
 */
public class TileMap {
	public final int startX, startY;
	public final int sizeX, sizeY, size;
	public int[] data;
	/**
	 * Creates a tile map with preloaded data
	 * @param startX @param startY position of upper left corner
	 * @param sizeX @param sizeY size
	 * @param data tile data
	 */
	public TileMap(int startX, int startY, int sizeX, int sizeY, int[] data) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.size = sizeX*sizeY;
		this.data = data;
	}
	
	/**
	 * Creates a tile map without preloaded data
	 * @param startX @param startY position of upper left corner
	 * @param sizeX @param sizeY size
	 */
	public TileMap(int startX, int startY, int sizeX, int sizeY) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.size = sizeX*sizeY;
		this.data = new int[size];
	}
	
	public boolean checkx(int x) {
		if(x < startX) return false;
		if(x - startX >= sizeX) return false;
		return true;
	}
	public boolean checky(int y) {
		if(y < startY) return false;
		if(y - startY >= sizeY) return false;
		return true;
	}
	public boolean check(int x, int y) {
		return checkx(x) && checky(y);
	}
	public boolean check(Point p) {
		return check(p.x, p.y);
	}
	
	/**
	 * Gets a tile on a specific world position
	 * @param x @param y world coordinates
	 * @return tile
	 */
	public int getAtPos(int x, int y) {
		return data[indexAtPos(x, y)];
	}
	
	/**
	 * Gets a tile on a specific array position
	 * @param x @param y array coordinates
	 * @return tile
	 */
	public int getAbsolute(int x, int y) {
		return data[x+y*sizeX];
	}
	
	public int indexAtPos(int x, int y) {
		return indexAbsolute(x - startX, y - startY);
	}
	public int indexAtPos(Point p) {
		return indexAtPos(p.x, p.y);
	}
	
	public int indexAbsolute(int x, int y) {
		return x+y*sizeX;
	}
	public int indexAbsolute(Point p) {
		return indexAbsolute(p.x, p.y);
	}
	
	

}
