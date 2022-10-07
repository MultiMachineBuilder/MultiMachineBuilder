/**
 * 
 */
package mmb.world.worlds.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.inventory.Inventory;
import mmb.world.visuals.Visual;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 * An implementation of {@code MapProxy} for the {@link World}
 */
public class WorldProxy implements MapProxy{

	public WorldProxy(World target) {
		that = target;
	}
	@Nonnull private World that;
	@SuppressWarnings("null")
	@Override
	public void close() {
		for(BlockChangeRequest ent: requests) {
			ent.apply0(that);
		}
		for(Runnable r: actions) {
			try {
				r.run();
			}catch(Exception e) {
				that.debug.pstm(e, "Failed to run the requested action");
			}
		}
		visuals.apply(that);
		that = null; //NOSONAR
	}
	@Override
	public void placeImmediately(BlockType block, int x, int y) {
		block.place(x, y, that);
	}
	@Override
	public void placeImmediatelyCreative(BlockType block, int x, int y) {
		//TODO block behavior
		placeImmediately(block, x, y);
	}
	@Override
	public void placeImmediately(BlockType block, int x, int y, Inventory inv) {
		//0.5 TODO Inventories
		//TODO block behavior
		placeImmediately(block, x, y);
	}
	@Override
	public BlockEntry get(int x, int y) {
		return that.get(x, y);
	}
	@Override
	public boolean inBounds(int x, int y) {
		return that.inBounds(x, y);
	}
	@Override
	public void place(BlockType block, int x, int y) {
		requests.add(new BlockChangeRequest(x, y, block));
	}
	@Override
	public void placeCreative(BlockType block, int x, int y) {
		//TODO block behavior
		place(block, x, y);
	}
	@Override
	public void place(BlockType block, int x, int y, Inventory inv) {
		//0.5 TODO Inventories
		//TODO block behavior
		place(block, x, y);
	}

	private List<BlockChangeRequest> requests = new ArrayList<>();
	private List<Runnable> actions = new ArrayList<>();
	@Override
	public void later(Runnable r) {
		actions.add(r);
	}
	@Override
	public int sizeX() {
		return that.sizeX;
	}
	@Override
	public int sizeY() {
		return that.sizeY;
	}
	@Override
	public int startX() {
		return that.startX;
	}
	@Override
	public int startY() {
		return that.startY;
	}
	@Override
	public World getMap() {
		return that;
	}
	
	private ModifyVisuals visuals = new ModifyVisuals();
	@Override
	public void add(Visual vis) {
		visuals.add(vis);
	}
	@Override
	public void adds(Collection<Visual> vis) {
		visuals.adds(vis);
	}
	@Override
	public void remove(Visual vis) {
		visuals.remove(vis);
	}
	@Override
	public void removes(Collection<Visual> vis) {
		visuals.removes(vis);
	}
}