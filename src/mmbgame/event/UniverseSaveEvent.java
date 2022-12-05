/**
 * 
 */
package mmbgame.event;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmbeng.worlds.universe.Universe;

/**
 * @author oskar
 *
 */
public class UniverseSaveEvent {
	public final ObjectNode node;
	public final String name;
	public final Universe world;
	public UniverseSaveEvent(ObjectNode node, String name, Universe world) {
		super();
		this.node = node;
		this.name = name;
		this.world = world;
	}
}
