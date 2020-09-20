package mmb.parts.tank;

import mmb.material.types.Fluid;
/**
 * 
 * @author oskar
 *
 *To instantiate, 	use @code new EmptyerHelper()
 *					 or @code new EmptyRefillHelper()
 *
 * @param <Q> type of quantity
 * @param <T> type of resource
 */
public interface Emptyer{
	public ChangeReport empty(double quantity, Fluid type);
	
}
