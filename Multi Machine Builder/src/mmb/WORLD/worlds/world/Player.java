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
		
		JsonNode alc1 = on.get("alcohol1");
		if(alc1 != null) digestibleAlcohol = alc1.asDouble();
		JsonNode alc2 = on.get("alcohol2");
		if(alc2 != null) BAC = alc2.asDouble();
		
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
		result.put("alcohol1", digestibleAlcohol);
		result.put("alcohol2", BAC);
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
		alcohol();
		blink();
		if(!(Double.isFinite(pos.x) && Double.isFinite(pos.y))){
			physics = new PlayerPhysicsNormal();
			pos.set(0, 0);
		}
		Vector2d posOld = new Vector2d(pos);
		speedTrue0.set(pos);
		double ctrlX=controls.x+jitter.x;
		double ctrlY=controls.y+jitter.y;
		physics.onTick(world, this, ctrlX, ctrlY);
		speedTrue0.sub(pos).mul(-50);
		if(Double.isFinite(pos.x) && Double.isFinite(pos.y)) return;
		physics = new PlayerPhysicsNormal();
		pos.set(posOld);
	}
	
	public final Vector2d controls = new Vector2d();
	public final Vector2d jitter = new Vector2d();

	/**
	 * Sets the control inputs
	 * @param u
	 * @param d
	 * @param l
	 * @param r
	 */
	public void setControls(boolean u, boolean d, boolean l, boolean r) {
		controls.set(0);
		if(u) controls.y--;
		if(d) controls.y++;
		if(l) controls.x--;
		if(r) controls.x++;
	}

	//Everything about alcohol
	private double digestibleAlcohol;
	/** @return the digestibleAlcohol amount of digestible alcohol remaining */
	public double getDigestibleAlcohol() {
		return digestibleAlcohol;
	}
	/** @param digestibleAlcohol set amount of alcohol that can be digested */
	public void setDigestibleAlcohol(double digestibleAlcohol) {
		this.digestibleAlcohol = digestibleAlcohol;
	}
	private double BAC;
	/**  @return the BAC */
	public double getBAC() {
		return BAC;
	}
	/** @param BAC the BAC to set */
	public void setBAC(double BAC) {
		this.BAC = BAC;
	}
	private void alcohol() {
		//Absorb the alcohol (0.1u/s)
		if(digestibleAlcohol < 0.002) {
			BAC += digestibleAlcohol;
			digestibleAlcohol = 0;
		}else {
			BAC += 0.002;
			digestibleAlcohol -= 0.002;
		}
		
		//Apply effects of alcohol
		//jitter
		double jitterscale = BAC*BAC / 10;
		double angle = 2*Math.PI*Math.random();
		double magnitude = jitterscale*Math.random();
		jitter.set(magnitude*Math.sin(angle), magnitude*Math.cos(angle));
		
		//blink speed
		if(BAC < 0) {
			BAC = 0;
			blinkspeed = 1;
		}else if(BAC < 4) {
			double ex = BAC-2;
			blinkspeed = 3-0.5*ex*ex;
		}else if(BAC < 4.5) {
			blinkspeed = 9-BAC*2;
		}else {
			blinkspeed = 0;
		}
		
		//Metabolize alcohol (0.02u/s)
		if(BAC < 0.0004) {
			BAC = 0;
		}else {
			BAC -= 0.0004;
		}
	}
	
	//Blinking
	private double blinkspeed = 0;
	private double blinkcycle = 0;
	private int blink = 0;
	private void blink() {
		//Blink cycle
		if(blink == 6) blink = 0;
		if(blink >= 1) 
			blink++;
		
		//Blink speed
		blinkcycle += blinkspeed/50;
		if(blinkcycle >= 1) {
			blinkcycle -= 1;
			blink = 1;
		}
	}

	/** @return current blink speed */
	public double getBlinkspeed() {
		return blinkspeed;
	}
	/** @return position of the blink cycle */
	public double getBlinkcycle() {
		return blinkcycle;
	}
	/** @return position of blink animation */
	public int getBlink() {
		return blink;
	}

	
}
