/**
 * 
 */
package mmb.world2.blockworld.direction;

/**
 * @author oskar
 *
 */
public enum DirHexa implements Direction<DirHexa> {
	NORTH, SOUTH, EAST, WEST, UP, DOWN;

	@Override
	public DirHexa turnUp() {
		switch(this) {
		case EAST:
		case NORTH:
		case SOUTH:
		case WEST:
			return UP;
		case UP:
		case DOWN:
		default:
			return null;
		}
	}

	@Override
	public DirHexa turnDown() {
		switch(this) {
		case EAST:
		case NORTH:
		case SOUTH:
		case WEST:
			return DOWN;
		case UP:
		case DOWN:
		default:
			return null;
		}
	}

	@Override
	public DirHexa turnUpsideDown() {
		switch(this) {
		case EAST:
		case NORTH:
		case SOUTH:
		case WEST:
			return this;
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		default:
			return null;
		}
	}
	public DirHexa turnLeft() {
		switch(this) {
		case EAST:
			return NORTH;
		case NORTH:
			return WEST;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		case UP:
		case DOWN:
			return this;
		default:
			return null;
		}
	}
	public DirHexa turnRight() {
		switch(this) {
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case UP:
		case DOWN:
			return this;
		default:
			return null;
		}
	}
	public DirHexa turnAround() {
		switch(this) {
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case UP:
		case DOWN:
			return this;
		default:
			return null;
		}
	}

	@Override
	public DirHexa bankLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirHexa bankRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirHexa bankAround() {
		// TODO Auto-generated method stub
		return null;
	}

	//Around cardinal directions
	
	//East-West
	@Override
	public DirHexa lateralUp() {
		switch(this) {
		case EAST:
		case WEST:
			return this;
		case NORTH:
			return UP;
		case SOUTH:
			return DOWN;
		case UP:
			return SOUTH;
		case DOWN:
			return NORTH;
		default:
			return null;
		}
	}

	@Override
	public DirHexa lateralDown() {
		switch(this) {
		case EAST:
		case WEST:
			return this;
		case NORTH:
			return DOWN;
		case SOUTH:
			return UP;
		case UP:
			return NORTH;
		case DOWN:
			return SOUTH;
		default:
			return null;
		}
	}

	@Override
	public DirHexa lateralAround() {
		switch(this) {
		case EAST:
		case WEST:
			return this;
		case NORTH:
			return SOUTH;
		case SOUTH:
			return NORTH;
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		default:
			return null;
		}
	}
	
	//Up-Down
	@Override
	public DirHexa verticalTurnLeft() {
		switch(this) {
		case EAST:
			return NORTH;
		case NORTH:
			return WEST;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		case UP:
		case DOWN:
			return this;
		default:
			return null;
		}
	}
	@Override
	public DirHexa verticalTurnRight() {
		switch(this) {
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case UP:
		case DOWN:
			return this;
		default:
			return null;
		}
	}
	@Override
	public DirHexa verticalTurnAround() {
		switch(this) {
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case UP:
		case DOWN:
			return this;
		default:
			return null;
		}
	}
	//North-South
	@Override
	public DirHexa polarBankLeft() {
		switch(this) {
		case EAST:
			return UP;
		case DOWN:
			return EAST;
		case UP:
			return WEST;
		case WEST:
			return DOWN;
		case NORTH:
		case SOUTH:
			return this;
		default:
			return null;
		}
	}

	@Override
	public DirHexa polarBankRight() {
		switch(this) {
		case EAST:
			return DOWN;
		case DOWN:
			return WEST;
		case UP:
			return EAST;
		case WEST:
			return UP;
		case NORTH:
		case SOUTH:
			return this;
		default:
			return null;
		}
	}

	@Override
	public DirHexa polarBankAround() {
		switch(this) {
		case EAST:
			return WEST;
		case DOWN:
			return UP;
		case UP:
			return DOWN;
		case WEST:
			return EAST;
		case NORTH:
		case SOUTH:
			return this;
		default:
			return null;
		}
	}

	@Override
	public DirHexa reverseEverything() {
		switch(this) {
		case EAST:
			return WEST;
		case DOWN:
			return UP;
		case UP:
			return DOWN;
		case WEST:
			return EAST;
		case NORTH:
			return SOUTH;
		case SOUTH:
			return NORTH;
		default:
			return null;
		}
	}

	@Override
	public String dirName() {
		switch(this) {
		case EAST:
			return "east";
		case DOWN:
			return "down";
		case UP:
			return "up";
		case WEST:
			return "west";
		case NORTH:
			return "north";
		case SOUTH:
			return "south";
		default:
			return "no direction";
		}
	}
	
	

}
