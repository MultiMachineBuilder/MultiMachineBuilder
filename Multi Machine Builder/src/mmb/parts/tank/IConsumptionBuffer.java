package mmb.parts.tank;


import mmb.material.types.Fluid;

/**
 * 
 * @author oskar
 *
 * @param <Q> type of quantity
 * @param <T> type of resource
 */
public interface IConsumptionBuffer extends Emptyer, FluidStorage {
	public void requestContents(double amount, Fluid resource);
	public void addAcceptableOutput(Fluid type);
}
