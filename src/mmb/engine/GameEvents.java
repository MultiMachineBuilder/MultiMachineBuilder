/**
 * 
 */
package mmb.engine;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.Event;

import io.vavr.Tuple2;
import mmb.engine.debug.Debugger;
import mmb.engine.worlds.universe.Universe;
import mmb.engine.worlds.world.World;

/**
 * A collection of game-wide events
 * @author oskar
 */
public class GameEvents {
	private GameEvents() {}
	@NonNull private static final Debugger debug = new Debugger("EVENTS");
	
	//Universes
	/** Invoked when a universe is created */
	@NonNull public static final Event<Universe> onUniverseCreate = new CatchingEvent<>(debug, "Failed to process universe created event");
	/** Invoked after a universe is loaded (includes a node to load from)*/
	@NonNull public static final Event<Tuple2<Universe, ObjectNode>> onUniverseLoad = new CatchingEvent<>(debug, "Failed to process universe loaded event");
	/** Invoked when a universe is saved (includes a node to save to)*/
	@NonNull public static final Event<Tuple2<Universe, ObjectNode>> onUniverseSave = new CatchingEvent<>(debug, "Failed to process universe saved event");
	/** Invoked when a universe is shut down */
	@NonNull public static final Event<Universe> onUniverseDie = new CatchingEvent<>(debug, "Failed to process universe died event");
	
	//Worlds
	/** Invoked when a world is created */
	@NonNull public static final Event<World> onWorldCreate = new CatchingEvent<>(debug, "Failed to process world created event");
	/** Invoked after a world is loaded (includes a node to load from)*/
	@NonNull public static final Event<Tuple2<World, ObjectNode>> onWorldLoad = new CatchingEvent<>(debug, "Failed to process world loaded event");
	/** Invoked when a world is saved (includes a node to save to)*/
	@NonNull public static final Event<Tuple2<World, ObjectNode>> onWorldSave = new CatchingEvent<>(debug, "Failed to process world saved event");
	/** Invoked when a world is shut down */
	@NonNull public static final Event<World> onWorldDie = new CatchingEvent<>(debug, "Failed to process world died event");
}
