package mmb.parts.tank.helpers;

import java.util.Hashtable;

import mmb.material.types.Fluid;
import mmb.parts.tank.ChangeReport;
import mmb.parts.tank.IConsumptionBuffer;
import mmb.parts.tank.IInputBuffer;
import mmb.parts.tank.Tank;

public class EmptyRefillHelper implements IConsumptionBuffer, IInputBuffer {
	Tank connected;
	Hashtable<Fluid, Double> contents;

	@Override
	public ChangeReport refill(Fluid toRefill, double quantity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestContents(double amount, Fluid resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * @see mmb.parts.tank.IInputBuffer.flush()
	 */
	public void flush() {
		contents.forEach((Fluid typ, Double amt) -> {connected.add(typ, amt); setAmount(typ, 0.0);});
	}

	@Override
	public void addAcceptableInput(Fluid type) {
		addAcceptableOutput(type);
	}

	@Override
	public void addAcceptableOutput(Fluid type) {
		contents.putIfAbsent(type, 0.0);
	}

	ChangeReport empty0(double old, double toRemove) {
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
	
	@Override
	public ChangeReport empty(double quantity, Fluid type) {
		ChangeReport result = empty0(contents.get(type), quantity);
		add(type, result.getIncrease());
		connected.add(type, result.getDecrease());
		return result;
	}

	@Override
	public Hashtable<Fluid, Double> getContents() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#setAmount(mmb.material.types.Fluid, double)
	 */
	@Override
	public void setAmount(Fluid target, double amount) {
		contents.put(target, amount);
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#getAmount(mmb.material.types.Fluid)
	 */
	@Override
	public double getAmount(Fluid target) {
		// TODO Auto-generated method stub
		return contents.get(target);
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#supports(mmb.material.types.Fluid)
	 */
	@Override
	public boolean supports(Fluid f) {
		return contents.containsKey(f);
	}

	
	
}
