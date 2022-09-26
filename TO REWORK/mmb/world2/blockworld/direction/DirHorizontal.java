/**
 * 
 */
package mmb.world2.blockworld.direction;

/**
 * @author oskar
 *
 */
public enum DirHorizontal implements Direction<DirHorizontal> {
	NORTH, SOUTH, EAST, WEST; //possibilities
	
	public DirHorizontal turnLeft() {
		switch(this) {
		case EAST:
			return NORTH;
		case NORTH:
			return WEST;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		default:
			return null;
		}
	}
	public DirHorizontal turnRight() {
		switch(this) {
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		default:
			return null;
		}
	}
	public DirHorizontal turnAround() {
		switch(this) {
		case EAST:
			return SOUTH;
		case NORTH:
			return EAST;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		default:
			return null;
		}
	}
	
	//Unsupported methods
	@Override
	public DirHorizontal turnUp() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal turnDown() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal turnUpsideDown() {
		// TODO Auto-generated method stub
		return turnAround();
	}
	@Override
	public DirHorizontal bankLeft() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal bankRight() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal bankAround() {
		// TODO Auto-generated method stub
		return this;
	}
	
	
	@Override
	public DirHorizontal lateralUp() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal lateralDown() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal lateralAround() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal verticalTurnLeft() {
		// TODO Auto-generated method stub
		return turnLeft();
	}
	@Override
	public DirHorizontal verticalTurnRight() {
		// TODO Auto-generated method stub
		return turnRight();
	}
	@Override
	public DirHorizontal verticalTurnAround() {
		// TODO Auto-generated method stub
		return turnAround();
	}
	@Override
	public DirHorizontal polarBankLeft() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal polarBankRight() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal polarBankAround() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public DirHorizontal reverseEverything() {
		return turnAround();
	}
	@Override
	public String dirName() {
		switch(this) {
		case EAST:
			return "East";
		case NORTH:
			return "North";
		case SOUTH:
			return "South";
		case WEST:
			return "West";
		default:
			return "No direction";
		
		}
	}
}
