/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.worlds.BlockChangeRequest;
import mmb.WORLD.worlds.MapProxy;

public class WorldProxy implements MapProxy{
	public WorldProxy(World target) {
		that = target;
	}
	@Nonnull private World that;
	@SuppressWarnings("null")
	@Override
	public void close() {
		BlockMap bm = that.getMap();
		for(BlockChangeRequest ent: requests) {
			ent.apply(that);
		}
		for(Runnable r: actions) {
			try {
				r.run();
			}catch(Exception e) {
				that.debug.pstm(e, "Failed to run the requested action");
			}
		}
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
		return that.sizeX();
	}
	@Override
	public int sizeY() {
		return that.sizeY();
	}
	@Override
	public int startX() {
		return that.startX();
	}
	@Override
	public int startY() {
		return that.startY();
	}
	@Override
	public BlockArrayProvider getMap() {
		return that.getMap();
	}
}