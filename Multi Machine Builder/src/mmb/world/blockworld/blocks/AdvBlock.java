/**
 * 
 */
package mmb.world.blockworld.blocks;

import e3d.d3.Size3;
import mmb.world.blockworld.blocks.factory.BlockSetupInfo;
import mmb.world.blockworld.direction.Direction;
import mmb.world.blockworld.direction.DirectionGuidance;
import mmb.world.blockworld.direction.NoRotation;
import mmb.world.blockworld.maps.WorldMap;

/**
 * @author oskar
 * This is advanced version of block. Works best for machines
 */
public abstract class AdvBlock implements Block {
	//Fields
	
	//World
	WorldMap world;
	Size3 pos = new Size3(0,0,0);
	DirectionGuidance rotation = new DirectionGuidance(NoRotation.NO_ROTATION);
	
	//Abstract methods
	
	abstract protected void makeBlock(BlockSetupInfo info);
	
	
	
	
	
	
	
	
	/**
	 * Create a blank block
	 */
	public AdvBlock() {}

	//Direction change methods
	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#turnUp()
	 */
	public void turnUp() {
		rotation.turnUp();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#turnDown()
	 */
	public void turnDown() {
		rotation.turnDown();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#turnUpsideDown()
	 */
	public void turnUpsideDown() {
		rotation.turnUpsideDown();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#turnAround()
	 */
	public void turnAround() {
		rotation.turnAround();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#turnLeft()
	 */
	public void turnLeft() {
		rotation.turnLeft();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#turnRight()
	 */
	public void turnRight() {
		rotation.turnRight();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#bankLeft()
	 */
	public void bankLeft() {
		rotation.bankLeft();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#bankRight()
	 */
	public void bankRight() {
		rotation.bankRight();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#bankAround()
	 */
	public void bankAround() {
		rotation.bankAround();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#lateralUp()
	 */
	public void lateralUp() {
		rotation.lateralUp();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#lateralDown()
	 */
	public void lateralDown() {
		rotation.lateralDown();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#lateralAround()
	 */
	public void lateralAround() {
		rotation.lateralAround();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#verticalTurnLeft()
	 */
	public void verticalTurnLeft() {
		rotation.verticalTurnLeft();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#verticalTurnRight()
	 */
	public void verticalTurnRight() {
		rotation.verticalTurnRight();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#verticalTurnAround()
	 */
	public void verticalTurnAround() {
		rotation.verticalTurnAround();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#polarBankLeft()
	 */
	public void polarBankLeft() {
		rotation.polarBankLeft();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#polarBankRight()
	 */
	public void polarBankRight() {
		rotation.polarBankRight();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#polarBankAround()
	 */
	public void polarBankAround() {
		rotation.polarBankAround();
	}

	/**
	 * 
	 * @see mmb.world.blockworld.direction.DirectionGuidance#reverseEverything()
	 */
	public void reverseEverything() {
		rotation.reverseEverything();
	}

	/**
	 * @return
	 * @see mmb.world.blockworld.direction.DirectionGuidance#dirName()
	 */
	public String dirName() {
		return rotation.dirName();
	}

	
	
	
	
	
	//Movement
	
	//Wrappers to make things easier
}
