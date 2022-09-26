package mmb.parts.tank;

import java.util.Hashtable;

import mmb.material.types.Fluid;
import mmb.parts.tank.helpers.ConsumptionBuffer;
import mmb.parts.tank.helpers.InputBuffer;

public interface FluidStorage {
	public Hashtable<Fluid,Double> getContents();
	public void setAmount(Fluid target, double amount);
	public double getAmount(Fluid target);
	default public void add(Fluid target, double amount) {
		setAmount(target, getAmount(target) + amount);
	}
	default public void substract(Fluid target, double amount) {
		setAmount(target, getAmount(target) - amount);
	}
	

	/**
	 * Make consumption buffer
	 * @return consumption buffer
	 */
	default public IConsumptionBuffer getConsumptionBuffer() {
		return new ConsumptionBuffer(this);
	}
	/**
	 * Make input buffer
	 * @return input buffer
	 */
	public default IInputBuffer getInputBuffer() {
		return new InputBuffer(this);
	}

	/**
	 * 
	 * @param f type of fluid
	 * @return
	 */
	public boolean supports(Fluid f);
	
	public static ChangeReport empty0(double old, double toRemove) {
		double namt = old;
		if(namt > toRemove) {
			//Enough
			namt = old-toRemove;
		}else {
			//Not enough
			namt = 0;
		}
		return new ChangeReport(old, namt);
	}
}
