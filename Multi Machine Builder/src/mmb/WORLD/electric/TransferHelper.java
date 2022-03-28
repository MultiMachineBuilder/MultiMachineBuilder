/**
 * 
 */
package mmb.WORLD.electric;

import com.google.common.util.concurrent.Runnables;

import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 * A class to help transfer electricity
 */
public class TransferHelper{
	public int maxIters = 500;
	public final double delta = 1e-8;
	private final BlockEntity blockent;
	public double power;
	public double pressure = 0;
	public double pressureWt = 1;
	private static final Debugger debug = new Debugger("TRANSFER HELPER");
	public TransferHelper(BlockEntity ent, double pwr) {
		this.blockent = ent;
		power = pwr;
	}

	/**
	 * Insert electricity recursively. Does not travel backwards
	 * @param map map with wires
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param amount amount to insert in coulombs
	 * @param iters current number of iterations
	 * @param volt voltage tier
	 * @param s insertion side
	 * @param blow when extracting, runs when element is overvoltaged
	 * @return amount transferred
	 */
	public double _transfer(World map, int x, int y, double amount, int iters, VoltageTier volt, Side s, Runnable blow) {
		if(iters > maxIters) return 0; //Iteration limit reached
		BlockEntry here = map.get(x, y);
		
		if(here instanceof Conduit) {
			//Here is a conduit
			Conduit cond = (Conduit)here;
			//debug.printl("Power pressure: "+cond.getTransfer().pressure+" at ["+x+","+y+"]"); //TF does not work - but readout is correct
			double sgn = Math.signum(amount);
			//debug.printl("Signum: "+sgn);
			//debug.printl("Amount: "+amount);
			double max = sgn*Math.min(sgn*amount, cond.condCapacity()); //The power limited by conduit
			//debug.printl("Max: "+max);
			double pressure0 = cond.getTransfer().pressure;
			
			double pdiffU = 0, pdiffD = 0, pdiffL = 0, pdiffR = 0;
			double pu = Double.NaN, pd = Double.NaN, pl = Double.NaN, pr = Double.NaN; 
			//Get power connections
			//Move up Y-
			Electricity eu = map.get(x, y-1).getElectricalConnection(Side.D);
			if(eu != null) {
				pu = eu.pressure();
				pdiffU = sgn*(pressure0 - pu); //curr - new pressure (new pressure > curr pressure -> negative result)
			}
			//Move down Y+
			Electricity ed = map.get(x, y+1).getElectricalConnection(Side.U);
			if(ed != null) {
				pd = ed.pressure();
				pdiffD = sgn*(pressure0 - pd);
			}
			//Move left X-
			Electricity el = map.get(x-1, y).getElectricalConnection(Side.R);
			if(el != null) {
				pl = el.pressure();
				pdiffL = sgn*(pressure0 - pl);
			}
			//Move right X+
			Electricity er = map.get(x+1, y).getElectricalConnection(Side.L);
			if(er != null) {
				pr = er.pressure();
				pdiffR = sgn*(pressure0 - pr);
			}
			
			//double[] pressures = {pu, pd, pl, pr};
			//debug.printl("Pressures (NaN means disconnected): "+Arrays.toString(pressures));
			
			//double[] incrs = {pdiffU, pdiffD, pdiffL, pdiffR};
			//debug.printl("Grades: "+Arrays.toString(incrs));
			
			//Cap the values
			if(pdiffU < 0) pdiffU = 0;
			if(pdiffD < 0) pdiffD = 0;
			if(pdiffL < 0) pdiffL = 0;
			if(pdiffR < 0) pdiffR = 0;
			
			//Calculate shares
			double sum = pdiffU+pdiffD+pdiffL+pdiffR;
			if(sum == 0) return 0; //Sum is always 0
			double shareU = pdiffU/sum;
			double totalU = shareU*max;
			double shareD = pdiffD/sum;
			double totalD = shareD*max;
			double shareL = pdiffL/sum;
			double totalL = shareL*max;
			double shareR = pdiffR/sum;
			double totalR = shareR*max;
			
			//double[] shares = {shareU, shareD, shareL, shareR};
			//debug.printl("Shares: "+Arrays.toString(shares));
			
			//Transfer
			double transferSum = 0;
			if(shareU > delta) transferSum += _transfer(map, x, y-1, totalU, iters+1, volt, Side.D, blow);
			if(shareD > delta) transferSum += _transfer(map, x, y+1, totalD, iters+1, volt, Side.U, blow);
			if(shareL > delta) transferSum += _transfer(map, x-1, y, totalL, iters+1, volt, Side.R, blow);
			if(shareR > delta) transferSum += _transfer(map, x+1, y, totalR, iters+1, volt, Side.L, blow);
			double remaining = max-transferSum;
			cond.getTransfer().pressure += remaining;
			return transferSum;
		}
		//Not a conduit, do not continue
		Electricity elec = here.getElectricalConnection(s);
		if(elec == null) return 0;
		return elec.transfer(amount, volt, blow);
	}
	public double transferSide(double amt, VoltageTier volt, Side s, Runnable blow) {
		double max = Math.min(power, Math.max(-power, amt));
		return _transfer(blockent.owner(), blockent.posX(), blockent.posY(), max, 0, volt, s, blow);
	}
	/**
	 * @param s side to which power goes
	 * @return electricity proxy for this transfer helper
	 */
	public Electricity proxy(Side s) {
		return new Electricity() {
			@Override
			public double insert(double amt, VoltageTier volt) {
				if(amt < 0) return 0;
				return transferSide(Math.min(amt, power), volt, s, Runnables.doNothing());
			}
			@Override
			public double extract(double amt, VoltageTier volt, Runnable blow) {
				if(amt < 0) return 0;
				return transferSide(-Math.min(amt, power), volt, s, blow);
			}
			@Override
			public VoltageTier voltage() {
				return VoltageTier.V9;
			}
			@Override
			public double pressure() {
				return pressure;
			}
			@Override
			public double pressureWeight() {
				return pressureWt;
			}
		};
	}

	/**
	 * @param tf
	 */
	public void set(TransferHelper tf) {
		maxIters = tf.maxIters;
		power = tf.power;
		pressure = tf.pressure;
		pressureWt = tf.pressureWt;
	}
}
