/**
 * 
 */
package mmb.engine.worlds.world;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.Event;
import com.rainerhahnekamp.sneakythrow.Sneaky;

import io.vavr.Tuple2;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.beans.Saver;
import mmb.data.variables.ListenableBoolean;
import mmb.engine.CatchingEvent;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.storage.ListenableSimpleInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.sound.Sound;
import mmb.engine.sound.Sounds;

/**
 * @author oskar
 * A {@code Player} is an object, which represents player data
 */
public class Player implements Saver{
	@NN private Debugger debug = new Debugger("PLAYERS");
	@NN private static final Debugger sdebug = new Debugger("PLAYERS");
	
	/**
	 * Creates a new player object
	 * @param w world, where player resides
	 */
	public Player(World w) {
		playerHP.addChangeListener(this::changeHP);
		smack = new CatchingEvent<>(debug, "Failed to process smack event");
		smack.addListener(this::smack);
		Sound sound = Sounds.getSound("377157__pfranzen__smashing-head-on-wall.ogg");
		Sneaky.sneaked(sound::open).accept(clip);
		Sound death = Sounds.getSound("220203__gameaudio__casual-death-loose.wav");
		Sneaky.sneaked(death::open).accept(deathClip);
		world = w;
	}
	
	/** The item inventory of this player */
	@NN public final ListenableSimpleInventory inv = new ListenableSimpleInventory(debug);
	/** The world where the player is located */
	@NN public final World world;
	
	//Game mode
	@NN public final ListenableBoolean creative = new ListenableBoolean();
	/** @return is the player creative mode? */
	public boolean isCreative() {
		return creative.getValue();
	}
	/** @return is the player survival mode? */
	public boolean isSurvival() {
		return !isCreative();
	}
	/**
	 * Sets whether creative mode is used
	 * @param value should creative mode be used?
	 */
	public void setCreative(boolean value) {
		creative.setValue(value);
	}

	/** Invoked when a player is saved */
	public static final Event<Tuple2<Player, ObjectNode>> onPlayerSaved
	= new CatchingEvent<>(sdebug, "Failed to save mod player data");
	/** Invoked when a player is loaded */
	public static final Event<Tuple2<Player, ObjectNode>> onPlayerLoaded
	= new CatchingEvent<>(sdebug, "Failed to load mod player data");
	
	public void load(@Nil JsonNode data) {
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
		debug = new Debugger("PLAYERS/"+world.getName());
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
	
	//Sound
	private Clip clip = Sneaky.sneak(AudioSystem::getClip);
	private Clip deathClip = Sneaky.sneak(AudioSystem::getClip);
	
	//Player physics
	/**
	 * The center position of the player
	 */
	@NN public final Vector2d pos = new Vector2d();
	@NN private final Vector2d speedTrue0 = new Vector2d();
	@NN public final Vector2dc speedTrue = speedTrue0;
	/** The physical speed of the player */
	@NN public final Vector2d speed = new Vector2d();
	/** The player physics mode */
	@NN public PlayerPhysics physics = new PlayerPhysicsNormal();
	void onTick(World world1) {
		blinkspeed = 1;
		blink();
		if(!(Double.isFinite(pos.x) && Double.isFinite(pos.y))){
			physics = new PlayerPhysicsNormal();
			pos.set(0, 0);
		}
		Vector2d posOld = new Vector2d(pos);
		speedTrue0.set(pos);
		
		//Input controls
		double ctrlX=controls.x+jitter.x;
		double ctrlY=controls.y+jitter.y;
		
		//Handle the physics model
		Vector2d oldSpeed = new Vector2d(speed);
		physics.onTick(world1, this, ctrlX, ctrlY);
		double speedDiff = oldSpeed.distance(speed);
		
		//Play head smacked sound when deccelerating more than 36 km/h in one tick
		if(speedDiff > 10) {
			clip.setFramePosition(0);
			clip.start();
			smack.trigger(speedDiff);
		}
		
		speedTrue0.sub(pos).mul(-50); //calculate true speed
		
		//reject infinite positions
		if(Double.isFinite(pos.x) && Double.isFinite(pos.y)) return;
		physics = new PlayerPhysicsNormal();
		pos.set(posOld);
	}
	
	/** The player control input */
	public final Vector2d controls = new Vector2d();
	/** The instantenous jitter */
	public final Vector2d jitter = new Vector2d();

	/**
	 * Sets the control inputs
	 * @param u move up
	 * @param d move down
	 * @param l move left
	 * @param r move right
	 */
	public void setControls(boolean u, boolean d, boolean l, boolean r) {
		controls.set(0);
		if(u) controls.y--;
		if(d) controls.y++;
		if(l) controls.x--;
		if(r) controls.x++;
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

	//HP and death
	/**
	 * The range model for HP
	 */
	public final BoundedRangeModel playerHP = new DefaultBoundedRangeModel(10000000, 0, 0, 10000000);
	/**
	 * Subtracts given amount from the HP
	 * @param amount amount of damage
	 */
	public void hurt(int amount) {
		playerHP.setValue(playerHP.getValue() - amount);
	}
	/**
	 * Triggered when player "smacks" into something
	 */
	public final Event<Double> smack;
	private void changeHP(ChangeEvent e) {
		if(playerHP.getValue() <= 0) death();
	}
	private void death() {
		//drop all items
		int x = (int)pos.x;
		int y = (int)pos.y;
		for(ItemRecord irecord: inv) {
			ItemEntry item = irecord.item();
			int extract = irecord.extract(Integer.MAX_VALUE);
			for(int i=0; i < extract; i++) {
				world.dropItem(item, x, y);
			}
		}
		
		//reset certain values
		pos.set(0);
		speed.set(0);
		playerHP.setValue(10000000);
		
		//play death sound
		deathClip.setFramePosition(0);
		deathClip.start();
		
		//log it
		debug.printl("DEATH");
	}
	private void smack(Double speed1) {
		double speed0 = speed1.doubleValue();
		hurt((int)(speed0*speed0*5000));
	}
}
