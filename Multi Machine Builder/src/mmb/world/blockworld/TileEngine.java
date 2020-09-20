/**
 * 
 */
package mmb.world.blockworld;

import java.io.IOException;

import mmb.items.VolatileInventory;

/**
 * @author oskar
 *
 */
public class TileEngine {

	protected int mapBufferID;

	public TileEngine(int mapBufferID) {
		super();
		this.mapBufferID = mapBufferID;
	}
	
	public void update() {
		VolatileInventory ZPIB = new VolatileInventory();
		
		try {
			ZPIB.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
