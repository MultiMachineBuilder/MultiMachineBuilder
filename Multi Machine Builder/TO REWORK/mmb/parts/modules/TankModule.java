package mmb.parts.modules;

import java.util.Hashtable;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import mmb.material.types.Fluid;
import mmb.parts.Part;
import mmb.parts.tank.Tank;
import mmb.parts.tank.helpers.ConsumptionBuffer;
import mmb.parts.tank.helpers.InputBuffer;
import mmb.world2.Circumstances;


public class TankModule implements Tank, PartModule{
	Hashtable<Fluid, Double> contents;
	Hashtable<Fluid, Double> capacity;
	@Override
	public ConsumptionBuffer getConsumptionBuffer() {
		return new ConsumptionBuffer(this);
	}

	@Override
	public InputBuffer getInputBuffer() {
		return new InputBuffer(this);
	}

	@Override
	public void setCapacityFor(mmb.material.types.Fluid f, double q) {
		capacity.put(f, q);
		contents.putIfAbsent(f, q);
	}

	@Override
	public Hashtable<Fluid, Double> getContents() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.parts.modules.PartModule#effects()
	 */
	@Override
	public PartModuleEffects effects() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.parts.tank.FluidStorage#setAmount(mmb.material.types.Fluid, double)
	 */
	@Override
	public void setAmount(Fluid target, double amount) {
		double max = capacity.get(target);
		if(amount > max) {
			contents.put(target, max);
		}else {
			contents.put(target, amount);
		}
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
		// TODO Auto-generated method stub
		return false;
	}

	
	
	@Override
	public void build() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flight(Circumstances cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partCreation(Part p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partCrash(Circumstances cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createGeometry(Geometry g, Spatial s) {
		// TODO Auto-generated method stub
		
	}
}
