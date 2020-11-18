/**
 * 
 */
package mmb.world2.blockworld.direction;

/**
 * @author oskar
 *
 */
public class DirectionGuidance {
	@SuppressWarnings("rawtypes")
	public Direction<Direction> dir;
	
	@SuppressWarnings("rawtypes")
	public DirectionGuidance(Direction<Direction> dir) {
		super();
		this.dir = dir;
	}

	public void turnUp() {
		dir = dir.turnUp();
	}
	public void turnDown() {
		dir = dir.turnDown();
	}
	public void turnUpsideDown() {
		dir = dir.turnUpsideDown();
	}
	public void turnAround() {
		dir = dir.turnAround();
	}
	public void turnLeft() {
		dir = dir.turnLeft();
	}
	public void turnRight() {
		dir = dir.turnRight();
	}
	public void bankLeft() {
		dir = dir.bankLeft();
	}
	public void bankRight() {
		dir = dir.bankRight();
	}
	public void bankAround() {
		dir = dir.bankAround();
	}
	public void lateralUp() {
		dir = dir.lateralUp();
	}
	public void lateralDown() {
		dir = dir.lateralDown();
	}

	public void lateralAround() {
		dir = dir.lateralAround();
	}

	public void verticalTurnLeft() {
		dir = dir.verticalTurnLeft();
	}

	public void verticalTurnRight() {
		dir = dir.verticalTurnRight();
	}

	public void verticalTurnAround() {
		dir = dir.verticalTurnAround();
	}

	public void polarBankLeft() {
		dir = dir.polarBankLeft();
	}

	public void polarBankRight() {
		dir = dir.polarBankRight();
	}
	public void polarBankAround() {
		dir = dir.polarBankAround();
	}
	public void reverseEverything() {
		dir = dir.reverseEverything();
	}
	public String dirName() {
		return dir.dirName();
	}
	public Direction get() {
		return dir;
	}

}
