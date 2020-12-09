/**
 * 
 */
package mmb.WORLD.tileworld.map;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import mmb.DATA.databuffer.Saver;
import mmb.DATA.databuffer.Savers;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.world.WorldDataProvider;

/**
 * @author oskar
 *
 */
public class TileMap implements WorldDataProvider{
	public final int startX, startY;
	public final int sizeX, sizeY, size;
	public MapEntry[] blocks;
	
	public List<Block> blockdata;
	@SuppressWarnings("unchecked")
	public static final Saver<TileMap>[] handlers = new Saver[256];
	/**
	 * Creates a tile map with preloaded blocks
	 * @param startX @param startY position of upper left corner
	 * @param sizeX @param sizeY size
	 * @param blocks tile blocks
	 */
	public TileMap(int startX, int startY, int sizeX, int sizeY, MapEntry[] data) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.size = sizeX*sizeY;
		this.blocks = data;
	}
	
	/**
	 * Creates a tile map without preloaded blocks
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
		this.blocks = new MapEntry[size];
	}
	
	public boolean checkx(int x) {
		if(x < startX) return false;
        return x - startX < sizeX;
    }
	public boolean checky(int y) {
		if(y < startY) return false;
        return y - startY < sizeY;
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
	public Block get2(int x, int y) {
		MapEntry z = get(x, y);
		if(z == null) return null;
		return z.getType();
	}
	@Override
	public MapEntry get(int x, int y) {
		if(check(x, y)) return blocks[indexAtPos(x, y)];
		return null;
	}
	
	/**
	 * Gets a tile on a specific array position
	 * @param x @param y array coordinates
	 * @return tile
	 */
	public MapEntry getAbsolute(int x, int y) {
		return blocks[x+y*sizeX];
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
	
	/*@Deprecated
	public static TileMap loadv1(DataInputStream dis) throws IOException {
		TileMap tm = Savers.mapSaver.read(dis);
		tm.blockdata.add(Blocks.air);
		tm.blockdata.add(Blocks.grass);
		return tm;
	}
	
	@Deprecated
	public static TileMap loadv2(DataInputStream dis) throws IOException {
		int id = dis.read();
		return handlers[id].read(dis);
	}
	
	@Deprecated
	public void save(OutputStream os) throws IOException {
		handlers[1].save(new DataOutputStream(os), this);
	}*/
	
	/*static {
		handlers[0] = Savers.mapSaver;
		handlers[1] = new Saver<TileMap>() {

			@Override
			public void save(DataOutputStream dos, TileMap data) throws IOException {
				TMSaver.save2(dos, data);
			}

			@Override
			public TileMap read(DataInputStream dis) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}*/
	public Point indexToArrCoords(int index) {
		int x = index % sizeX;
		int y = index - x;
		y /= sizeX;
		return new Point(x, y);
	}
	public Point indexToWrldCoords(int index) {
		Point p = indexToArrCoords(index);
		p.x += startX;
		p.y += startY;
		return p;
	}

	@Override
	public void set(int x, int y, MapEntry me) {
		if(check(x, y)) blocks[indexAtPos(x, y)] = me;
	}

}
