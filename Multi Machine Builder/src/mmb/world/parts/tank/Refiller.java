package mmb.world.parts.tank;


import mmb.material.types.Fluid;

/**
 * 
 * @author oskar
 *
 * @param <Q> type of quantity
 * @param <T> type of resource
 */
public interface Refiller extends ContentManipulator {
	public ChangeReport refill(Fluid toRefill, double quantity);
}
