/**
 * 
 */
package mmb.WORLD.player;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import io.vavr.Tuple2;
import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.WORLD.inventory.SimpleInventory;
import mmb.debug.Debugger;

/**
 * @author oskar
 * A {@code Player} is an object, which represents player data
 */
public class Player implements GameObject, Saver<JsonNode>, Loader<JsonNode> {
	private static final Debugger debug = new Debugger("PLAYERS");

	@Override
	public String id() {
		return "Player";
	}

	@Override
	public String getUTID() {
		return "Player";
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameObject getOwner() {
		return null;
	}
	
	@Nonnull public final SimpleInventory inv = new SimpleInventory();
	@Nonnull public final ListenerBooleanVariable creative = new ListenerBooleanVariable();

	public static final Event<Tuple2<Player, ObjectNode>> onPlayerSaved
	= new CatchingEvent<>(debug, "Failed to save mod player data");
	public static final Event<Tuple2<Player, ObjectNode>> onPlayerLoaded
	= new CatchingEvent<>(debug, "Failed to save mod player data");
	@Override
	public void load(JsonNode data) {
		ObjectNode on = (ObjectNode) data;
		JsonNode nodeCreative = on.get("creative");
		if(nodeCreative != null) creative.setValue(nodeCreative.asBoolean(true));
		inv.capacity = 128;
		inv.load(JsonTool.requestArray("inventory", on));
		onPlayerLoaded.trigger(new Tuple2<>(this, on));
	}

	@Override
	public JsonNode save() {
		ObjectNode result = JsonTool.newObjectNode();
		onPlayerSaved.trigger(new Tuple2<>(this, result));
		result.set("inventory", inv.save());
		result.put("creative", creative.getValue());
		return result;
	}
}
