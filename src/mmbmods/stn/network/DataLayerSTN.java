/**
 * 
 */
package mmbmods.stn.network;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.data.json.JsonTool;
import mmb.world.worlds.world.DataLayer;
import mmb.world.worlds.world.World;

/**
 * The brains of Simple Transportation Network
 * @author oskar
 */
public class DataLayerSTN extends DataLayer<World> {

	/**
	 * Creates a Simple Transportation Network
	 * @param world
	 */
	public DataLayerSTN(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public @Nonnull JsonNode save() {
		ObjectNode node = JsonTool.newObjectNode();
		
		
		
		return node;
	}

	@Override
	public void load(JsonNode data) {
		// TODO Auto-generated method stub
		
	}
}
