/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.GameObject;
import mmb.BEANS.Positioned;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * For machines, use {@link mmb.WORLD.machine.Machine}. For blocks, use {@link BlockEntityType}
 * Events: <ul>
 * 	<li>Motion: runs when block entity is moved</li>
 * 	<li>Demolition: runs when block entity is mined</li>
 * 	<li></li>
 * 	<li></li>
 * </ul>
 */
public abstract class BlockEntity implements BlockEntry, Positioned, Cloneable {
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	//Positioning
	private int x, y;
	@Override
	public int posX() {
		return x;
	}
	@Override
	public void resetMap(@Nullable World map, int x, int y) {
		if(map != null && (!map.get(x, y).isSurface())) throw new IllegalStateException("The position ["+x+","+y+"] on target map is not surface");
		owner = map;
		this.x = x;
		this.y = y;
	}
	@Override
	public final BlockEntry blockCopy() {
		return clone();
	}
	@Override
	public int posY() {
		return y;
	}
	@Nonnull public Point getPosition() {
		return new Point(x, y);
	}
	
	//Rendering
	public BlockDrawer getTexture() {
		return type().getTexture();
	}
		
	//Containment
	/**
	 * The map, in which the BlockEntity is located
	 */
	@Nullable private World owner;
	@SuppressWarnings("null")
	@Nonnull public World owner() {
		if(owner == null) throw new IllegalStateException("Not placed in map");
		return owner;
	}
	@Nullable public World nowner() {
		return owner;
	}
	
	//Block entity methods
	/**
	 * Checks if BlockEntity is of given type
	 * @param type type to check
	 * @return is given BlockEntity of given type?
	 */
	public boolean typeof(BlockEntityType type) {
		return type() == type;
	}
	@Override
	public BlockEntity nasBlockEntity() {
		return this;
	}
	@Override
	public boolean isBlockEntity() {
		return true;
	}
	@Override
	public BlockEntity asBlockEntity() {
		return this;
	}	
	
	public BlockEntry getAtSide(Side s) {
		return owner().getAtSide(s, x, y);
	}
	
	/**
	 * Creates a copy of this block entity.
	 * The returned object has no owner and position, so it can be immediately pasted.
	 */
	@Override
	@Nonnull public BlockEntity clone(){
		try {
			BlockEntity copy = (BlockEntity)super.clone();
			copy.x = 0;
			copy.y = 0;
			copy.owner = null;
			copy.demoListeners = new ArrayList<>();
			return copy;
		}catch(CloneNotSupportedException e) {
			throw new InternalError(e); //last safeguard, if Cloneable fails
		}
	}
	
	/**
	 * Invoken on every tick
	 * @param map map proxy
	 */
	public void onTick(MapProxy map) {
		//Optional
	}
	
	//Block demolition event
	private boolean underDemolition;
	private List<BlockEntityDemolitionListener> demoListeners = new ArrayList<>();
	/**
	 * Adds a block entity demolition listener
	 * @param listener
	 */
	public void addBlockEntityDemolitionListener(BlockEntityDemolitionListener listener) {
		if(underDemolition) return;
		demoListeners.add(listener);
	}
	/**
	 * Removes a block entity demolition listener
	 * @param listener listener to remove
	 */
	public void removeBlockEntityDemolitionListener(BlockEntityDemolitionListener listener) {
		if(underDemolition) return;
		demoListeners.remove(listener);
	}
	@Override
	public final void onBreak(World blockMap, @Nullable GameObject obj) {
		if(underDemolition) return;
		BlockEntityDemolitionEvent event = new BlockEntityDemolitionEvent(x, y, this, blockMap, obj);
		for(BlockEntityDemolitionListener listener: demoListeners) {
			listener.blockDemolished(event);
		}
	}
}
