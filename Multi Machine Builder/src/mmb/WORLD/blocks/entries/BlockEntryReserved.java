/**
 * 
 */
package mmb.WORLD.blocks.entries;

import java.awt.Point;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * @author oskar
 *
 */
public class BlockEntryReserved extends BlockEntryAbstract{
	public Point location = new Point();

	@Override
	public JsonElement save(JsonElement e) {
		// TODO Auto-generated method stub
		return null;
	}

	public BlockEntryReserved(Point location) {
		this.location = location;
	}
	public BlockEntryReserved(int x, int y) {
		location.x = x;
		location.y = y;
	}
	
	public BlockEntryReserved() {}
}
