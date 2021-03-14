/**
 * 
 */
package mmb.WORLD.machine;

import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntity;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public abstract class SkeletalMachine implements Machine {
	protected int x, y;
	protected World map;
	protected BlockEntry[] left;
	protected BlockEntry[] right;
	protected BlockEntry[] up;
	protected BlockEntry[] down;
	
	@Override
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	protected void resetSides() {
		int sX = sizeX();
		int sY = sizeY();
		int eX = x + sX;
		int eY = y + sY;
		//Left
		left = new SkeletalBlockEntity[sY];
		for(int i = x, j = 0; i < eX; i++, j++) {
			left[j] = map.get(x-1, i);
		}
		right = new SkeletalBlockEntity[sY];
		for(int i = x, j = 0; i < eX; i++, j++) {
			right[j] = map.get(eX, i);
		}
		up = new SkeletalBlockEntity[sX];
		for(int i = y, j = 0; i < eY; i++, j++) {
			up[j] = map.get(eX, i);
		}
		down = new SkeletalBlockEntity[sX];
		for(int i = y, j = 0; i < eY; i++, j++) {
			down[j] = map.get(eX, i);
		}
	}
	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int posX() {
		return x;
	}

	@Override
	public int posY() {
		return y;
	}

	@Override
	public World getMap() {
		return map;
	}

	@Override
	public void setMap(World map) {
		this.map = map;
	}
	
	//Helper methods
	/**
	 * Get a block at given index 
	 * @param s
	 * @param offset
	 * @return block at given offset on a side
	 */
	public BlockEntry atSide(Side s, int offset) {
		switch(s) {
		case D:
			return map.get(x+offset, y+sizeY());
		case DL:
			return map.get(x-1, y+sizeY());
		case DR:
			return map.get(x+sizeX(), y+sizeY());
		case L:
			return map.get(x-1, y+offset);
		case R:
			return map.get(x+sizeX(), y+offset);
		case U:
			return map.get(x+offset, y-1);
		case UL:
			return map.get(x-1, y-1);
		case UR:
			return map.get(x+sizeX(), y-1);
		default:
			return map.get(x, y);
		}
	}

}
