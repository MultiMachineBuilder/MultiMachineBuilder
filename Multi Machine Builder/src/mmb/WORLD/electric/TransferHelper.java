/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Point;

import javax.annotation.Nonnull;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;
import sun.security.util.Debug;

/**
 * @author oskar
 * A class to help transfer electricity
 */
public class TransferHelper extends Battery {
	public int maxIters = 500;
	private final BlockMap map;
	private final int x, y;
	private static final Debugger debug = new Debugger("CONDUITS");
	public TransferHelper(BlockMap map, int x, int y, double cap, double pwr) {
		super(pwr, cap);
		this.map = map;
		this.x = x;
		this.y = y;
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
	public double _insert(Side to, BlockMap map, @Nonnull Point pt, double amount, int iters) {
		if(iters > maxIters) return 0; //Iteration limit reached
		BlockEntry here = map.get(pt);
		
		if(BlockEntry.isValidConduit(here)) {
			debug.printl("cap = "+PowerMeter.formatOut(here.condCapacity() * 50)+" "+pt.x+" "+pt.y);
			//Here is a conduit.
			//Sides: top, left, right
			Side left = to.ccw();
			Side right = to.cw();
			double remain = Math.min(amount, here.condCapacity());
			debug.printl("pwr0 = "+PowerMeter.formatOut(remain * 50)+" "+pt.x+" "+pt.y);
			//Forward
			Point fwd = to.offset(pt);
			if(remain <= 0) {
				debugpower(pt, amount, 'F');
				return amount;
			}
			double tfd1 = _insert(to, map, fwd, remain, iters+1);
			remain -= tfd1;
			
			//Left
			Point toleft = left.offset(pt);
			if(remain <= 0) {
				debugpower(pt, amount, 'L');
				return amount;
			}
			double tfd2 = _insert(left, map, toleft, remain, iters+1);
			remain -= tfd2;
			
			//Right
			Point toright = right.offset(pt);
			if(remain <= 0) {
				debugpower(pt, amount, 'R');
				return amount;
			}
			double tfd3 = _insert(right, map, toright, remain, iters+1);
			remain -= tfd3;
			debugpower(pt, amount - remain, ' ');
			return amount - remain;
		}
		//Not a conduit, do not continue
		Electricity elec = here.getElectricalConnection(to.negate());
		if(elec == null) return 0;
		return elec.insert(amount);
	}
	private static void debugpower(Point pt, double pwr, char side) {
		debug.printl(side+"power = "+PowerMeter.formatOut(pwr * 50)+" "+pt.x+" "+pt.y);
	}
	public double insertSide(double amt, Side to) {
		double max = Math.min(amt, maxPower);
		double battins = insert(max);
		if(battins == max) return max;
		double remain = max - battins;
		return battins + _insert(to.negate(), map, new Point(x, y), remain, 0);
	}
	/**
	 * @param s side to which power goes
	 * @return
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
