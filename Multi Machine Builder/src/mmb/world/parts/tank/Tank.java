package mmb.world.parts.tank;


import mmb.material.types.Fluid;

/**
 * 
 * @author oskar
 *
 * @param <Q> type of quantity
 * @param <T> type of resource
 * For implementation, see mmb.container.helpers.TankModule
 */
public interface Tank extends FluidStorage {
	
	/**
	 * To instantiate, use @code new TankModule<T>();
	 */

	
	/**
	 * Add/change storage capacity
	 * @param f type of fluid
	 * @param q capacity
	 */
	public void setCapacityFor(Fluid f, double q);

	
	
	
}
