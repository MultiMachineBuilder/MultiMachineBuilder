/**
 * 
 */
package mmb.WORLD.tileworld.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import mmb.WORLD.tileworld.Side;
import mmb.WORLD.tileworld.TileGUI;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.math.PositionUtils;

/**
 * @author oskar
 * Block proxy contains all world methods which can be used with blocks.
 */
public class BlockProxy {
	public WorldDataProvider data;
	protected TileGUI gui;
	private final List<BlockPos> scheduledChanges = new ArrayList<BlockPos>();
	private boolean available;
	public static class BlockPos {
		public MapEntry b;
		public Point p;
		public BlockPos(MapEntry b, Point p) {
			super();
			this.b = b;
			this.p = p;
		}
	}

	public BlockProxy(WorldDataProvider dp) {
		data = dp;
	}
	
	public void place(Block block, Point pos) {
		set(block, pos);
	}
	
	public void remove(Point pos) {
		Block b = getBlock(pos);
		Block d = b.leaveBehind;
		place(d, pos);
	}
	
	public void set(mmb.WORLD.tileworld.block.MapEntry b, Point pos) {
		set(new BlockPos(b, pos));
	}
	public void set(BlockPos change) {
		if(available)scheduledChanges.add(change);
	}
	public void setImmediately(Block block, Point pos) {
		data.set(pos, block);
	}
	public void setImmediately(BlockPos change) {
		data.set(change);
	}
	public void apply() {
		available = false;
		for (BlockPos change : scheduledChanges) {
			setImmediately(change);
		}
		scheduledChanges.clear();
		available = true;
	}
	
	public Block getBlock(Point pos) {
		MapEntry result = getEntry(pos);
		if(result == null) return null;
		return result.getType();
	}
	public MapEntry getEntry(Point pos) {
		return data.get(pos);
	}
	public MapEntry getAtSide(Point p, Side s) {
		return get(PositionUtils.getPosAtSide(p, s));
	}
	public MapEntry get(Point p) {
		return data.get(p);
	}
	public MapEntry get(int x, int y) {
		return data.get(x, y);
	}

	public Block[] getNeighbors4(Point pos) {
		List<Block> result = new ArrayList<Block>();
		tryAddSide(result, pos, Side.D);
		tryAddSide(result, pos, Side.L);
		tryAddSide(result, pos, Side.U);
		tryAddSide(result, pos, Side.R);
		Block[] ret = new Block[result.size()];
		for(int i = 0; i < result.size(); i++) {
			ret[i] = result.get(i);
		}
		return ret;
	}
	public MapEntry[] getNeighbors4a(Point pos) {
		List<Block> result = new ArrayList<Block>();
		tryAddSide(result, pos, Side.D);
		tryAddSide(result, pos, Side.L);
		tryAddSide(result, pos, Side.U);
		tryAddSide(result, pos, Side.R);
		Block[] ret = new Block[result.size()];
		for(int i = 0; i < result.size(); i++) {
			ret[i] = result.get(i);
		}
		return ret;
	}
	public Block[] getNeighbors8(Point pos) {
		List<Block> result = new ArrayList<Block>();
		tryAddSide(result, pos, Side.D);
		tryAddSide(result, pos, Side.DL);
		tryAddSide(result, pos, Side.L);
		tryAddSide(result, pos, Side.UL);
		tryAddSide(result, pos, Side.U);
		tryAddSide(result, pos, Side.UR);
		tryAddSide(result, pos, Side.R);
		tryAddSide(result, pos, Side.DR);
		Block[] ret = new Block[result.size()];
		for(int i = 0; i < result.size(); i++) {
			ret[i] = result.get(i);
		}
		return ret;
	}
	public MapEntry[] getNeighbors8a(Point pos) {
		List<MapEntry> result = new ArrayList<MapEntry>();
		tryAddSide2(result, pos, Side.D);
		tryAddSide2(result, pos, Side.DL);
		tryAddSide2(result, pos, Side.L);
		tryAddSide2(result, pos, Side.UL);
		tryAddSide2(result, pos, Side.U);
		tryAddSide2(result, pos, Side.UR);
		tryAddSide2(result, pos, Side.R);
		tryAddSide2(result, pos, Side.DR);
		Block[] ret = new Block[result.size()];
		for(int i = 0; i < result.size(); i++) {
			ret[i] = result.get(i).getType();
		}
		return ret;
	}
	private void tryAddSide(List<Block> target, Point pos, Side s) {
		// file deepcode ignore ApiMigration~getBlock: suggested method absent		Block x = getBlock(PositionUtils.getPosAtSide(pos, s));
		if(x != null) target.add(x);
	}
	private void tryAddSide2(List<MapEntry> target, Point pos, Side s) {
		MapEntry x = get(PositionUtils.getPosAtSide(pos, s));
		if(x != null) target.add(x);
	}
	
	/**
	 * 
	 * @param corners should corners be included
	 * @param pos position
	 * @return number of neighbours
	 */
	public int countNeighbours(boolean corners, Point pos, Block target) {
		int n = 0;
		//ALL CASES
		if(getAtSide(pos, Side.U).getType() == target) n++;
		if(getAtSide(pos, Side.D).getType() == target) n++;
		if(getAtSide(pos, Side.L).getType() == target) n++;
		if(getAtSide(pos, Side.R).getType() == target) n++;
		if(corners) {
			if(getAtSide(pos, Side.UL).getType() == target) n++;
			if(getAtSide(pos, Side.UR).getType() == target) n++;
			if(getAtSide(pos, Side.DL).getType() == target) n++;
			if(getAtSide(pos, Side.DR).getType() == target) n++;
		}
		return n;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Block getBlock(int x, int y) {
		MapEntry me = data.get(x, y);
		if(me == null) return null;
		return me.getType();
	}

}
