/**
 * 
 */
package mmb.tilemap;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author oskar
 *
 */
public interface TileMap {
	public Tile getTile(Point pos);
	public void setTile(Point pos, Tile tile);
	public boolean isStored(Point pos);
	public boolean checkBounds(Point pos);
	public Dimension tileSize();
	public boolean isAvaliable();
	
	public TileMapSubsection subsectionBound(Rectangle rect);
	public BasicTileMap subsectionUnBound(Rectangle rect);
	public Rectangle getBounds();
}
