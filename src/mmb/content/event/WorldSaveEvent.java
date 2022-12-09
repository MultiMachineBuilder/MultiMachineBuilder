/**
 * 
 */
package mmb.content.event;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.engine.worlds.world.World;

/**
 * @author oskar
 *
 */
public class WorldSaveEvent {
	public final ObjectNode node;
	public final String name;
	public final World world;
	public WorldSaveEvent(ObjectNode node, String name, World world) {
		super();
		this.node = node;
		this.name = name;
		this.world = world;
	}
}
