/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Point;

import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * A class to help transfer electricity
 */
public class TransferHelper extends Battery {
	public int maxIters = 500;
	private final BlockEntity blockent;
	public TransferHelper(BlockEntity ent, double cap, double pwr) {
		super(pwr, cap);
		this.blockent = ent;
	}

	/**
	 * Insert electricity recursively. Does not travel backwards
	 * @param to direction of travel
	 * @param map map with wires
	 * @param pt position
	 * @param amount amount to extract
	 * @param iters current number of iterations
	 * @return amount transferred
	 */
	public double _insert(Side to, World map, Point pt, double amount, int iters) {
		if(iters > maxIters) return 0; //Iteration limit reached
		BlockEntry here = map.get(pt);
		
		if(BlockEntry.isValidConduit(here)) {
			//Here is a conduit.
			//Sides: top, left, right
			Side left = to.ccw();
			Side right = to.cw();
			double max = Math.min(amount, here.condCapacity()); //The power limited by conduit
			double remain = max;
			//Forward
			Point fwd = to.offset(pt);
			double tfd1 = _insert(to, map, fwd, remain, iters+1);
			remain -= tfd1;
			if(remain <= 0) return max;
						
			//Left
			Point toleft = left.offset(pt);		
			double tfd2 = _insert(left, map, toleft, remain, iters+1);
			remain -= tfd2;
			if(remain <= 0) return max;
			
			//Right
			Point toright = right.offset(pt);
			double tfd3 = _insert(right, map, toright, remain, iters+1);
			remain -= tfd3;
			return max - remain;
		}
		//Not a conduit, do not continue
		Electricity elec = here.getElectricalConnection(to.negate());
		if(elec == null) return 0;
		return elec.insert(amount);
	}
	public double insertSide(double amt, Side to) {
		double max = Math.min(amt, maxPower);
		double battins = insert(max);
		if(battins == max) return max;
		double remain = max - battins;
		return battins + _insert(to.negate(), blockent.owner(), blockent.getPosition(), remain, 0);
	}
	/**
	 * @param s side to which power goes
	 * @return electricity proxy for this transfer helper
	 */
	public Electricity proxy(Side s) {
		Side n = s.negate();
		return new Electricity() {
			@Override
			public double amount() {
				return TransferHelper.this.amount();
			}
			@Override
			public double capacity() {
				return TransferHelper.this.capacity();
			}
			@Override
			public double insert(double amt) {
				return insertSide(Math.min(amt, maxPower), n);
			}
		};
	}
}
