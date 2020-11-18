package mmb.parts.tank.helpers;

import java.util.Hashtable;

import mmb.material.types.Fluid;
import mmb.parts.tank.ChangeReport;
import mmb.parts.tank.FluidStorage;
import mmb.parts.tank.IConsumptionBuffer;

public class ConsumptionBuffer implements IConsumptionBuffer {
	Hashtable<Fluid,Double> amount = new Hashtable<Fluid, Double>();
	FluidStorage associated;
	public ConsumptionBuffer(FluidStorage cont) {
		associated = cont;
	}

	
	
	@Override
	public ChangeReport empty(double quantity, Fluid type) {
		ChangeReport result = FluidStorage.empty0(amount.get(type), quantity);
		add(type, result.getIncrease());
		return result;
	}

	@Override
	public void requestContents(double amount, Fluid resource) {
		ChangeReport tmp = FluidStorage.empty0(associated.getAmount(resource), amount);
		associated.setAmount(resource, tmp.getCurrent());
		add(resource, tmp.getDecrease());
	}

	@Override
	public void addAcceptableOutput(Fluid type) {
		amount.put(type, 0.0);
	}

	@Override
	public Hashtable<Fluid, Double> getContents() {
		// TODO Auto-generated method stub
		return amount;
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#setAmount(mmb.material.types.Fluid, double)
	 */
	@Override
	public void setAmount(Fluid target, double amount) {
		this.amount.put(target, amount);
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#getAmount(mmb.material.types.Fluid)
	 */
	@Override
	public double getAmount(Fluid target) {
		return amount.get(target);
	}


	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#supports(mmb.material.types.Fluid)
	 */
	@Override
	public boolean supports(Fluid f) {
		return amount.containsKey(f);
	}

}
