/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Point;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.vavr.Tuple2;
import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.RUNTIME.TaskLoop;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.gui.FPSCounter;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockLoader;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.player.Player;
import mmb.WORLD.worlds.DataLayers;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.universe.Universe;
import mmb.debug.Debugger;
import monniasza.collects.Identifiable;
import monniasza.collects.SetProxy;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * <br>
 * A {@code BlockMap} is a representation of ingame 2D voxel map.
 * <br> To load the map, create and the use the {@code saveLoad} module
 * <br> ,its {@code getLoaded()} method for getting loaded {@code BlockMap}
 * <br> ,its {@code load()} method to load data
 * <br> and at first {@code setName()} to set target name
 */
public class World implements Identifiable<String>, BlockArrayProviderSupplier{
	protected Debugger debug = new Debugger("MAP anonymous");
	public final FPSCounter tps = new FPSCounter();
	private static final String txtMAP = "MAP - ";
	public final Player player = new Player();
	//[start] naming
	private String name;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(@Nullable String name) {
		this.name = name;
		if(name == null) {
			debug.printl("Became anonymous");
			debug.id = "MAP anonymous";
		}else{
			debug.printl("Renamed to: "+name);
			debug.id = txtMAP + name;
		}
	}
	@Override
	public String id() {
		return name;
	}
	//[end]
	//[start] owner
	private Universe owner;
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Universe owner) {
		this.owner = owner;
	}
	/**
	 * @return the owner of this world
	 */
	@Override
	public World getOwner() {
		return this;
	}
	/**
	 * @return the associated universe
	 */
	public Universe getUniverse() {
		return owner;
	}
	//[end]
	//[start] constructors
	/**
	 * Creates a world with given entries and specific starting position
	 * @param entries world data
	 * @param startX starting X
	 * @param startY starting Y
	 */
	public World(BlockEntry[][] entries, int startX, int startY) {
		setMap(new BlockMap(this, entries, startX, startY));
	}
	/**
	 * Create an empty world with given bounds
	 * @param sizeX horizontal size
	 * @param sizeY vertical size
	 * @param startX starting X
	 * @param startY starting Y
	 */
	public World(int sizeX, int sizeY, int startX, int startY) {
		setMap(new BlockMap(this, sizeX, sizeY, startX, startY));
	}
	/**
	 * Creates a world without a map
	 */
	public World() {}
	public World(BlockMap map) {
		setMap(map);
	}
	//[end]
	//[start] serialization
	public final Serialization saveLoad = new Serialization();
	public class Serialization implements Saver<@Nonnull JsonNode>, Loader<@Nonnull JsonNode>{
		private Serialization() {}	
		@Override
		public void load(JsonNode json) {
			
			//Dimensions
			int sizeX = json.get("sizeX").asInt();
			int sizeY = json.get("sizeY").asInt();
			int startX = json.get("startX").asInt();
			int startY = json.get("startY").asInt();
			int endX = sizeX + startX;
			int endY = sizeY + startY;
			
			//Prepare the map
			setMap(new BlockMap(World.this, sizeX, sizeY, startX, startY));
			
			//Blocks
			ArrayNode worldArray = (ArrayNode) json.get("world");
			BlockLoader bloader = new BlockLoader();
			bloader.map = map;
			// deepcode ignore UsingEmptyCollection: must be correct size
			Iterator<JsonNode> iter = worldArray.elements();
			for(int y = startY; y < endY; y++) {
				for(int x = startX; x < endX; x++) {
					JsonNode node = iter.next();
					bloader.x = x;
					bloader.y = y;
					BlockEntry block = bloader.load(node);
					map.set(block, x, y);
				}
			}
			
			//Machines
			ArrayNode machines = (ArrayNode) json.get("machines");
			for(JsonNode machineNode: machines) {
				ArrayNode aMachine = (ArrayNode) machineNode;
				String type = aMachine.get(0).asText();
				int x = aMachine.get(1).asInt();
				int y = aMachine.get(2).asInt();
				JsonNode machineData  = aMachine.get(3);
				try {
					Machine machine = MachineModel.forID(type).initialize(x, y, machineData);
					placeMachine(machine);
				} catch(Exception e) {
					debug.pstm(e, "Failed to place a machine of type "+type+" at ["+x+","+y+"]");
				}
			}
			
			//Data layers
			ObjectNode dataNode = (ObjectNode) json.get("data");
			data.addAll(DataLayers.createAllMapDataLayers());
			for(WorldDataLayer dl: data) {
				JsonNode datalayerData = dataNode.get(dl.id());
				try {
					if(!datalayerData.isMissingNode()) dl.load(datalayerData);
					dl.afterMapLoaded(World.this);
				} catch (Exception e) {
					debug.pstm(e, "Failed to load data layer "+dl.title());
				}
			}
			
			WorldEvents.load.trigger(new Tuple2<World, ObjectNode>(World.this, (ObjectNode) json));
			
			//Player data
			player.load(JsonTool.requestObject("player", (ObjectNode) json));
			
			//After loading process
			WorldEvents.afterLoad.trigger(World.this);
		}
		@Override
		public JsonNode save() {
			//Master node
			ObjectNode master = JsonTool.newObjectNode();
			
			//Dimensions
			int sX = sizeX();
			int sY = sizeY();
			int pX = startX();
			int pY = startY();
			int eX = sX + pX;
			int eY = sY + pY;
			master.set("sizeX", new IntNode(sX));
			master.set("sizeY", new IntNode(sY));
			master.set("startX", new IntNode(pX));
			master.set("startY", new IntNode(pY));
			
			//Blocks
			ArrayNode blockArrayNode = JsonTool.newArrayNode();
			for(int y = pY; y < eY; y++) {
				for(int x = pX; x < eX; x++) {
					BlockEntry ent = get(x, y);
					blockArrayNode.add(ent.save());
				}
			}
			master.set("world", blockArrayNode);
			
			//Machines
			ArrayNode machineArrayNode = JsonTool.newArrayNode();
			for(Machine m: map._machines) {
				ArrayNode array = JsonTool.newArrayNode();
				//format: [id, x, y, data]
				array.add(m.id());
				array.add(m.posX());
				array.add(m.posY());
				array.add(m.save());
				machineArrayNode.add(array);
			}
			master.set("machines", machineArrayNode);
			
			//Data layers
			ObjectNode dataNode = JsonTool.newObjectNode();
			for(WorldDataLayer mdl: data) {
				dataNode.set(mdl.id(), mdl.save());
			}
			master.set("data", dataNode);
			
			//Player
			master.set("player", player.save());
			
			WorldEvents.save.trigger(new Tuple2<World, ObjectNode>(World.this, master));
			return master;
		}
	}
	//[end]
	//[start] activity
	@SuppressWarnings("null")
	private void update() {//Run the map
		//debug.printl("update");
		tps.count();
		//Set up the proxy
		try(MapProxy proxy = createProxy()){
			map.update(proxy); //Run every machine
		}catch(Exception e) {
			debug.pstm(e, "Failed to run the map");
		}
	}
	private final static long PERIOD = 20_000_000; //nanoseconds
	private TaskLoop timer = new TaskLoop(() -> update(), PERIOD);
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return timer.getState() == 1;
	}
	/**
	 * Starts the timer.
	 * @throws IllegalStateException when timer is running or destroyed.
	 */
	public void startTimer() {
		timer.start();
	}
	/**
	 * Destroy all map resources
	 */
	public void destroy() {
		debug.printl("Destroying");
		shutdown();
		setMap(null);
	}
	/**
	 * @return the hasShutDown
	 */
	public boolean hasShutDown() {
		return timer.getState() == 2;
	}
	/**
	 * Shut down the map, but keep the resources
	 */
	@SuppressWarnings("null")
	public void shutdown() {
		debug.printl("Shutdown");
		if(hasShutDown()) return;
		//Shut down block entities
		for(BlockEntity ent: map.getBlockEntities()) {
			try {
				ent.onShutdown(map);
			} catch (Exception e) {
				debug.pstm(e, "Failed to shut down block "+ent.type().id()+" at ["+ent.posX()+","+ent.posY()+"]");
			}
		}
		//Shut down machines
		for(Machine m: machines) {
			try {
				m.onShutdown();
			}catch(Exception e) {
				debug.pstm(e, "Failed to shut down machine "+m.id()+" at ["+m.posX()+","+m.posY()+"]");
			}
		}
		if(isRunning()) timer.destroy();
	}
	//[start] [DEPRECATED] machines (moved to BlockMap)
	SetProxy<Machine> machinesProxy = new SetProxy<>();
	/**
	 * An unmodifiable set of all machines
	 * @deprecated Machine functionality moved to {@link BlockMap}. This set now works as a proxy
	 */
	@Deprecated
	public final Set<Machine> machines = machinesProxy.proxy;
	/**
	 * Remove the machine at given location
	 * @param p location
	 * @return was machine removed?
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link BlockMap#removeMachine(Point)} instead
	 */
	public boolean removeMachine(Point p) {
		return map.removeMachine(p);
	}
	/**
	 * Remove the machine at given location
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return was machine removed?
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link BlockMap#removeMachine(int,int)} instead
	 */
	public boolean removeMachine(int x, int y) {
		return map.removeMachine(x, y);
	}
	/**
	 * Place the machine, using the machine model, with default properties
	 * @param m machine model which creates the machine
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return newly placed machine, or null if placement failed
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link BlockMap#placeMachine(MachineModel,int,int)} instead
	 */
	public Machine placeMachine(MachineModel m, int x, int y) {
		return map.placeMachine(m, x, y);
	}
	/**
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link mmb.WORLD.worlds.world.BlockMap#placeMachine(Machine)} instead
	 */
	public Machine placeMachine(Machine machine) {
		return map.placeMachine(machine);
	}
	/**
	 * 
	 * @param machine machine to place
	 * @param setup if true, call onLoad. Else call onPlace
	 * @return newly placed machine, or null if placement failed
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link mmb.WORLD.worlds.world.BlockMap#placeMachine(mmb.WORLD.worlds.world.World,Machine,boolean)} instead
	 */
	public Machine placeMachine(Machine machine, boolean setup) {
		return map.placeMachine(machine, setup);
	}
	/**
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link mmb.WORLD.worlds.world.BlockMap#placeMachineLoaded(mmb.WORLD.worlds.world.World,Machine)} instead
	 */
	public Machine placeMachineLoaded(Machine machine) {
		return map.placeMachineLoaded(machine);
	}
	/**
	 * Place the machine, using the machine model, with default properties
	 * @param m machine model which creates the machine
	 * @param p position
	 * @return newly placed machine, or null if placement failed
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link BlockMap#placeMachine(MachineModel,Point)} instead
	 */
	public Machine placeMachine(MachineModel m, Point p) {
		return map.placeMachine(m, p);
	}
	/**
	 * Get a machine based on coordinates
	 * @param p machine coordinates
	 * @return machine, or null if not found
	 * @deprecated Machine functionality moved to {@link BlockMap}. Use {@link BlockMap#getMachine(Point)} instead
	 */
	@Nullable
	public Machine getMachine(Point p) {
		return map.getMachine(p);
	}
	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return machine, or null if not found
	 * @deprecated Machine functionality moved to {@link BlockMap}.
	 * Use {@link BlockMap#getMachine(int,int)} instead
	 */
	@Nullable
	public Machine getMachine(int x, int y) {
		return map.getMachine(x, y);
	}
	//[end]
	//[start] data layers
		public final SelfSet<String,WorldDataLayer> data = new HashSelfSet<>();
	//[end]
	//[start] actions
	/**
	 * Left click the given block
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return did click pass through?
	 */
	@Override
	public boolean click(int x, int y) {
		BlockEntry ent = get(x, y);
		boolean result = ent instanceof BlockActivateListener;
		if(result) {
			((BlockActivateListener) ent).click(x, y, this);
		}
		return result;
	}
	//[end]
	
	
	@Override
	public World parent() {
		return this;
	}

	private BlockMap map;
	
	@Override
	public BlockMap getMap() {
		return map;
	}
	/**
	 * Sets the block map.
	 * Provide {@code null} to remove a block map
	 * @param map the new map
	 */
	public void setMap(@Nullable BlockMap map) {
		this.map = map;
		if(map == null) {
			machinesProxy.set = null;
		}else {
			machinesProxy.set = map.machines;
		}
		
	}
	/**
	 * Create a map proxy intended for temporary use
	 * @return newly created map proxy
	 */
	public MapProxy createProxy() {
		return new WorldProxy(this);
	}
}