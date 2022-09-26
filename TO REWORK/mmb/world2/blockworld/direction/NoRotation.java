/**
 * 
 */
package mmb.world2.blockworld.direction;

/**
 * @author oskar
 *
 */
public enum NoRotation implements Direction<NoRotation> {
	NO_ROTATION;

	@Override
	public NoRotation turnUp() {
		return this;
	}

	@Override
	public NoRotation turnDown() {
		return this;
	}

	@Override
	public NoRotation turnUpsideDown() {
		return this;
	}

	@Override
	public NoRotation turnAround() {
		return this;
	}

	@Override
	public NoRotation turnLeft() {
		return this;
	}

	@Override
	public NoRotation turnRight() {
		return this;
	}

	@Override
	public NoRotation bankLeft() {
		return this;
	}

	@Override
	public NoRotation bankRight() {
		return this;
	}

	@Override
	public NoRotation bankAround() {
		return this;
	}

	@Override
	public NoRotation lateralUp() {
		return this;
	}

	@Override
	public NoRotation lateralDown() {
		return this;
	}

	@Override
	public NoRotation lateralAround() {
		return this;
	}

	@Override
	public NoRotation verticalTurnLeft() {
		return this;
	}

	@Override
	public NoRotation verticalTurnRight() {
		return this;
	}

	@Override
	public NoRotation verticalTurnAround() {
		return this;
	}

	@Override
	public NoRotation polarBankLeft() {
		return this;
	}

	@Override
	public NoRotation polarBankRight() {
		return this;
	}

	@Override
	public NoRotation polarBankAround() {
		return this;
	}

	@Override
	public NoRotation reverseEverything() {
		return this;
	}
	
	@Override
	public String dirName() {
		return "No direction";
	}
}
