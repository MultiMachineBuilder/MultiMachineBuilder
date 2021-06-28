/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import mmb.BEANS.RunOnTick;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.worlds.MapProxy;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * A collection of blocks and block entities
 */
public class BlockMap implements BlockArrayProvider{
	/**
	 * 
	 */
	@Nonnull private final World world;
	//[start] Blocks
	private Set<BlockEntity> _blockents = new HashSet<>();
	/**
	 * an unmodifiable {@link Set} of {@link BlockEntity}s on this {@code BlockMap}
	 */
	private final Set<BlockEntity> blockents = Collections.unmodifiableSet(_blockents);
	
	private Set<RunOnTick> runs = new HashSet<>();
	
	protected Grid<@Nonnull BlockEntry> entries;
	@Override
	public BlockEntry get(int x, int y) {
		return entries.get(x-startX, y-startY);
	}
	/**
	 * Get a block using array index
	 * @param i horizontal array index
	 * @param j vertical array index
	 * @return block at given index
	 */
	public BlockEntry getindex(int i, int j) {
		return entries.get(i, j);
	}
	@SuppressWarnings({"unlikely-arg-type"})
	@Override
	public BlockEntry set(BlockEntry b, int x, int y) {
		BlockEntry old = get(x, y);
		//Remove old block entity
		if(old.isBlockEntity()) {
			BlockEntity old0 = old.asBlockEntity();
			try {
				old0.onBreak(this, null);
				_blockents.remove(old0);
			} catch (Exception e) {
				this.world.debug.pstm(e, "Failed to remove BlockEntity ["+x+","+y+"]");
				return null;
			}
		}
		if(old instanceof RunOnTick) {
			runs.remove(old);
		}
		//Add new block entity
		if(b.isBlockEntity()) {
			BlockEntity new0 = b.asBlockEntity();
			try {
				new0.onPlace(this, null);
				_blockents.add(new0);
			}catch(Exception e) {
				this.world.debug.pstm(e, "Failed to place BlockEntity ["+x+","+y+"]");
				entries.set(x-startX, y-startY, ContentsBlocks.grass);
				return null;
			}
		}
		if(b instanceof RunOnTick) 
			runs.add((RunOnTick) b);
		
		//Set block
		entries.set(x-startX, y-startY, b);
		return b;
	}
	@Override
	public BlockEntry place(BlockType type, int x, int y) {
		BlockEntry blockent = type.create(x, y, this);
		return set(blockent, x, y);
	}
	@Override
	public boolean isValid() {
		return entries != null;
	}
	//[end]		
	//[start] Constructors
	/**
	 * Creates a world with given entries and specific starting position
	 * @param entries world data
	 * @param startX starting X
	 * @param startY starting Y
	 * @param world enclosing world
	 */
	public BlockMap(World world, BlockEntry[][] entries, int startX, int startY) {
		this.world = world;
		this.entries = new FixedGrid<>(entries);
		this.startX = startX;
		this.startY = startY;
		sizeX = entries.length;
		sizeY = entries[0].length;
		endX = startX + sizeX;
		endY = startY + sizeY;
	}
	/**
	 * Create an empty world with given bounds
	 * @param sizeX horizontal size
	 * @param sizeY vertical size
	 * @param startX starting X
	 * @param startY starting Y
	 * @param world enclosing world
	 */
	public BlockMap(World world, int sizeX, int sizeY, int startX, int startY) {
		this.world = world;
		this.entries = new FixedGrid<>(sizeX, sizeY);
		entries.fill(ContentsBlocks.grass);
		this.startX = startX;
		this.startY = startY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		endX = startX + sizeX;
		endY = startY + sizeY;
	}
	//[end]	
	@Override
	public Set<BlockEntity> getBlockEntities() {
		return blockents;
	}
	//[start] bounds
	/** Starting X coordinate, inclusive */
	public final int startX;
	/** Starting Y coordinate, inclusive */
	public final int startY;
	/** Map width */
	public final int sizeX;
	/** Map height */
	public final int sizeY;
	/** Ending X coordinate, exclusive*/
	public final int endX;
	/** Ending Y coordinate, exclusive*/
	public final int endY;
	
	@Override
	public int startX() {
		return startX;
	}
	@Override
	public int startY() {
		return startY;
	}
	@Override
	public int sizeX() {
		return sizeX;
	}
	@Override
	public int sizeY() {
		return sizeY;
	}
	@Override
	public boolean inBounds(int x, int y) {
		if(x < startX) return false;
		if(y < startY) return false;
		if(x >= endX) return false;
		return y < endY;
	}
	//[end]
	@Override
	@Nonnull public World parent() {
		return this.world;
	}
	/**
	 * @return the owner
	 */
	@Override
	public World getOwner() {
		return world;
	}
	void update(MapProxy proxy) {
		if(entries == null) return;
		for(Machine m: _machines) {
			try {
				m.onUpdate(proxy);
			}catch(Exception e) {
				this.world.debug.pstm(e, "Failed to run a machine");
			}
		}
		//Run every block
		for(RunOnTick ent: runs) {
			try {
				long start = System.nanoTime();
				ent.onTick(proxy);
				long finish = System.nanoTime();
				long duration = finish - start;
				if(duration >= 1_000_000) {
					if(ent instanceof BlockEntity) {
						this.world.debug.printl("Block entity at ["+((BlockEntity) ent).posX()+","+((BlockEntity) ent).posY()+
								"] took exceptionally long to run");
					}else {
						this.world.debug.printl("Block "+ent.toString()+" took exceptionally long to run");
					}
				}
			} //NOSONAR
			catch(Exception e){
				if(ent instanceof BlockEntity) {
					this.world.debug.pstm(e, "Failed to run block entity at ["
							+((BlockEntity) ent).posX()+","+((BlockEntity) ent).posY()+"]");
				}else {
					this.world.debug.pstm(e, "Failed to run a block");
				}
				
			}
		}
	}
	
	//[start] Machines
	/**
	 * Remove the machine at given location
	 * @param p location
	 * @return was machine removed?
	 */
	public boolean removeMachine(Point p) {
		Machine machine = machinesPoints.get(p);
		if(machine == null) return false;
		//Calculate coordinates
		int posX = machine.posX();
		int posY = machine.posY();
		int mendX = posX + machine.sizeX();
		int mendY = posY + machine.sizeY();
		try {
			machine.onRemove(null);
		}catch(Exception e) {
			this.world.debug.pstm(e, "Failed to remove "+machine.id()+" at ["+posX+","+posY+"]");
			return false;
		}
		
		//Remove
		for(int x = posX; x < mendX; x++) {
			for(int y = posY; y < mendY; y++) {
				Point pt = new Point(x, y);
				machinesPoints.remove(pt);
			}
		}
		_machines.remove(machine);
		return true;
	}
	/**
	 * Remove the machine at given location
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return was machine removed?
	 */
	public boolean removeMachine(int x, int y) {
		return removeMachine(new Point(x, y));
	}
	/**
	 * Place the machine, using the machine model, with default properties
	 * @param m machine model which creates the machine
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachine(MachineModel m, int x, int y) {
		Machine machine = m.place();
		if(machine == null) return null;
		machine.setPos(x, y);
		return placeMachine(machine);
	}
	/**
	 * Places a machine which is already located
	 * @param machine machine to place
	 * @return placed machine
	 */
	public Machine placeMachine(Machine machine) {
		return placeMachine(machine, false);
	}
	/**
	 * 
	 * @param machine machine to place
	 * @param setup if true, call onLoad. Else call onPlace
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachine(Machine machine, boolean setup) {
		machine.setMap(this.world);
		int posX = machine.posX();
		int posY= machine.posY();
		int mendX = posX + machine.sizeX();
		int mendY = posY + machine.sizeY();
		try {
			if(!setup) machine.onPlace(null);
			//When successfull, place
			for(int x = posX; x < mendX; x++) {
				for(int y = posY; y < mendY; y++) {
					Point pt = new Point(x, y);
					Machine old = machinesPoints.put(pt, machine);
					if(old != null) {
						//Overwriting existing machine, do not place the machine
						machinesPoints.put(pt, old);
						return null;
					}
				}
			}
		}catch(Exception e) {
			this.world.debug.pstm(e, "Failed to place "+machine.id()+" at ["+posX+","+posY+"]");
			return null; //null indicates that machine was not placed
		}
		this.world.debug.printl("Placed machine");
		_machines.add(machine);
		return machine;
	}
	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return machine, or null if not found
	 */
	@Nullable
	public Machine getMachine(int x, int y) {
		return getMachine(new Point(x, y));
	}
	/**
	 * Gets machine at given location
	 * @param p point to check
	 * @return machine at given point, or null if none
	 */
	@Nullable
	public Machine getMachine(Point p) {
		return machinesPoints.get(p);
	}
	/**
	 * Place the machine, using the machine model, with default properties
	 * @param m machine model which creates the machine
	 * @param p position
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachine(MachineModel m, Point p) {
		return placeMachine(m, p.x, p.y);
	}
	/**
	 * @param machine machine to be placed
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachineLoaded(Machine machine) {
		return placeMachine(machine, true);
	}
	
	Set<Machine> _machines = new HashSet<>();
	/**
	 * The immutable set of all machines
	 */
	public final Set<Machine> machines = Collections.unmodifiableSet(_machines);
	
	
	/**
	 * @param item item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropItem(ItemEntry item, int x, int y) {
		drops.put(new Point(x, y), item);
	}
	/**
	 * The multimap containing all dropped items
	 */
	public final Multimap<Point, ItemEntry> drops = HashMultimap.create();
	//[end]
	//[start] [DEPRECATED] machines (moved to BlockMap)
	Map<Point, Machine> machinesPoints = new HashMap<>();
	
	//[end]
}