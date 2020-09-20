package mmb.parts.tank.helpers;

import java.util.Hashtable;

import mmb.material.types.Fluid;
import mmb.parts.tank.ChangeReport;
import mmb.parts.tank.FluidStorage;
import mmb.parts.tank.IInputBuffer;

public class InputBuffer implements IInputBuffer {
	FluidStorage connected;
	Hashtable<Fluid, Double> contents;
	/**
	 * create input buffer
	 * @param fs target
	 */
	public InputBuffer(FluidStorage fs) {
		connected = fs;
	}
	@Override
	/*
	 * (non-Javadoc)
	 * @see mmb.parts.tank.IInputBuffer#flush()
	 */
	public void flush() {
		contents.forEach((Fluid typ, Double amt) -> {connected.add(typ, amt); setAmount(typ, 0.0);});
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see mmb.parts.tank.IInputBuffer#addAcceptableInput(mmb.material.types.Fluid)
	 */
	public void addAcceptableInput(Fluid type) {
		contents.putIfAbsent(type, 0.0);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#getContents()
	 */
	public Hashtable<Fluid, Double> getContents() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.Refiller#refill(mmb.material.types.Fluid, double)
	 */
	@Override
	public ChangeReport refill(Fluid toRefill, double quantity) {
		double amt = contents.get(toRefill);
		ChangeReport result = new ChangeReport(amt, amt+quantity);
		add(toRefill, quantity);
		return result;
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
