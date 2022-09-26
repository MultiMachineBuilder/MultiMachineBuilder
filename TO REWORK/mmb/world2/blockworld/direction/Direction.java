/**
 * 
 */
package mmb.world2.blockworld.direction;

/**
 * @author oskar
 * This interface is mainly intended for enumerations.
 */
public interface Direction<T>{
	//Around local directions
	public T turnUp();
	public T turnDown();
	public T turnUpsideDown();
	
	public T turnAround();
	public T turnLeft();
	public T turnRight();
	
	public T bankLeft();
	public T bankRight();
	public T bankAround();
	
	//Around cardinal directions
	
	public T lateralUp();
	public T lateralDown();
	public T lateralAround();
	
	public T verticalTurnLeft();
	public T verticalTurnRight();
	public T verticalTurnAround();
	
	public T polarBankLeft();
	public T polarBankRight();
	public T polarBankAround();
	
	public T reverseEverything();
	
	public String dirName();
}
