/**
 * 
 */
package mmb.WORLD.worlds.world;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import io.vavr.Tuple2;
import mmb.GameObject;
import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.worlds.player.PlayerPhysics;
import mmb.WORLD.worlds.player.PlayerPhysicsNormal;
import mmb.debug.Debugger;

/**
 * @author oskar
 * A {@code Player} is an object, which represents player data
 */
public class Player implements GameObject, Saver<JsonNode> {
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
	public boolean isCreative() {
		return creative.getValue();
	}
	public boolean isSurvival() {
		return !isCreative();
	}
	public void setCreative(boolean value) {
		creative.setValue(value);
	}

	public static final Event<Tuple2<Player, ObjectNode>> onPlayerSaved
	= new CatchingEvent<>(debug, "Failed to save mod player data");
	public static final Event<Tuple2<Player, ObjectNode>> onPlayerLoaded
	= new CatchingEvent<>(debug, "Failed to load mod player data");
	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		ObjectNode on = (ObjectNode) data;
		
		JsonNode nodeCreative = on.get("creative");
		if(nodeCreative != null) creative.setValue(nodeCreative.asBoolean(true));
		
		inv.load(JsonTool.requestArray("inventory", on));
		inv.setCapacity(128);
		
		JsonNode posX = on.get("x");
		if(posX != null) pos.x = posX.asDouble();
		JsonNode posY = on.get("y");
		if(posY != null) pos.y = posY.asDouble();
		
		onPlayerLoaded.trigger(new Tuple2<>(this, on));
	}

	@Override
	public JsonNode save() {
		ObjectNode result = JsonTool.newObjectNode();
		onPlayerSaved.trigger(new Tuple2<>(this, result));
		result.set("inventory", inv.save());
		result.put("creative", creative.getValue());
		result.put("x", pos.x);
		result.put("y", pos.y);
		return result;
	}
	
	//Player physics
	/**
	 * The center position of the player
	 */
	@Nonnull public final Vector2d pos = new Vector2d();
	@Nonnull private final Vector2d speedTrue0 = new Vector2d();
	@Nonnull public final Vector2dc speedTrue = speedTrue0;
	@Nonnull public final Vector2d speed = new Vector2d();
	@Nonnull public PlayerPhysics physics = new PlayerPhysicsNormal();
	void onTick(World world) {
		if(!(Double.isFinite(pos.x) && Double.isFinite(pos.y))){
			physics = new PlayerPhysicsNormal();
			pos.set(0, 0);
		}
		Vector2d posOld = new Vector2d(pos);
		speedTrue0.set(pos);
		physics.onTick(world, this, ctrlR, ctrlD);
		speedTrue0.sub(pos).mul(-50);
		if(Double.isFinite(pos.x) && Double.isFinite(pos.y)) return;
		physics = new PlayerPhysicsNormal();
		pos.set(posOld);
	}
	
	int ctrlD, ctrlR;

	/**
	 * @param u
	 * @param d
	 * @param l
	 * @param r
	 */
	public void setControls(boolean u, boolean d, boolean l, boolean r) {
		ctrlD = 0;
		if(u) ctrlD--;
		if(d) ctrlD++;
		ctrlR = 0;
		if(l) ctrlR--;
		if(r) ctrlR++;
	}
}
