/**
 * 
 */
package mmb.content.electric;

import com.google.common.util.concurrent.Runnables;

import mmb.NN;
import mmb.content.electric.Electricity.SettablePressure;
import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntry;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.World;

/**
 * Implements the load balancing and electricity transport capabilities of the power conduit
 * @author oskar
 * @see mmb.content.electric.BlockConduit power conduit
 */
public class TransferHelper implements SettablePressure{
	public int maxIters = 500;
	public static final double delta = 1e-8;
	@NN private final BlockEntity blockent;
	/** Power limit in coulombs per tick*/
	public final double power;
	private double pressure = 0;
	private final double pressureWt;
	@NN private final VoltageTier volt;
	/**
	 * Creates a transfer helper
	 * @param ent owner of this transfer helper
	 * @param pwr maximum current in coulombs per tick
	 * @param volt maximum voltage tier
	 * @param pressureWt pressure weight
	 */
	public TransferHelper(BlockEntity ent, double pwr, VoltageTier volt, double pressureWt) {
		this.blockent = ent;
		power = pwr;
		this.volt = volt;
		this.pressureWt = pressureWt;
	}

	/**
	 * Insert or extracts electricity recursively. Does not travel backwards
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
		
		if(here instanceof BlockConduit) {
			//Here is a conduit
			BlockConduit cond = (BlockConduit)here;
			
			//Check voltage
			if(cond.volt.compareTo(volt) < 0) {
				cond.blow(); //The conduit was overvoltaged
				return 0;
			}
			
			double sgn = Math.signum(amount);
			double max = sgn*Math.min(sgn*amount, cond.condCapacity()/volt.volts); //The power limited by conduit
			double pressure0 = cond.getTransfer().pressure;
			
			double pdiffU = 0, pdiffD = 0, pdiffL = 0, pdiffR = 0;
			double pu = Double.NaN, pd = Double.NaN, pl = Double.NaN, pr = Double.NaN; 
			//Get power connections
			//Move up Y-
			Electricity eu = map.get(x, y-1).getElectricalConnection(Side.D);
			if(eu != null) {
				pu = eu.pressure();
				pdiffU = sgn*(pressure0 - pu);
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
	 * Creates an access proxy
	 * @param s side to which power goes
	 * @return electricity proxy for this transfer helper
	 */
	@NN public Electricity proxy(Side s) {
		return new Electricity() {
			@Override
			public double insert(double amt, VoltageTier volt) {
				if(amt < 0) return 0;
				if(volt.compareTo(voltage()) > 0) {
					//The conduit was overvoltaged
					blockent.blow();
					return 0;
				}
				return transferSide(Math.min(amt, power), volt, s, Runnables.doNothing());
			}
			@Override
			public double extract(double amt, VoltageTier volt, Runnable blow) {
				if(amt < 0) return 0;
				return -transferSide(-Math.max(amt, power), volt, s, blow);
			}
			@Override
			public VoltageTier voltage() {
				return volt;
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
	 * Replaces state of this transfer helper with the another one's
	 * @param tf source of data
	 */
	public void set(TransferHelper tf) {
		maxIters = tf.maxIters;
		pressure = tf.pressure;
	}

	
	@Override
	public double insert(double amt, VoltageTier volt) {
		return _transfer(blockent.owner(), blockent.posX(), blockent.posY(), amt, 0, volt, Side.U, Runnables.doNothing());
	}

	@Override
	public double extract(double amt, VoltageTier volt, Runnable blow) {
		return -_transfer(blockent.owner(), blockent.posX(), blockent.posY(), -amt, 0, volt, Side.U, Runnables.doNothing());
	}

	@Override
	public @NN VoltageTier voltage() {
		return volt;
	}

	@Override
	public double pressure() {
		return pressure;
	}

	@Override
	public double pressureWeight() {
		return pressureWt;
	}

	@Override
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
}
