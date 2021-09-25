/**
 * 
 */
package mmb.WORLD.electric;

import mmb.WORLD.block.Block;
import mmb.WORLD.rotate.Side;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class TestElectricity extends Block {

	@Override
	public Electricity getElectricalConnection(Side s) {
		return elec;
	}
	
	public static final Electricity elec = new Electricity() {
		private final Debugger debug = new Debugger("ELECTRICITY");
		@Override
		public double amount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double capacity() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double insert(double amt) {
			debug.printl("Trying to insert: "+amt);
			return 0;
		}
		
	};

}
