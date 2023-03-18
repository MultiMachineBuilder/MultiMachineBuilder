/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Color;
import java.awt.Graphics;

import mmb.content.ContentsBlocks;
import mmb.content.electric.Electricity;
import mmb.content.electric.VoltageTier;
import mmb.engine.UnitFormatter;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * Measures electrical power in a debug screen. Works at any voltage
 * @author oskar
 */
public class PowerMeter extends BlockEntityRotary {
	//Contents
	private double counter;
	private String label;
	private final Electricity IN = new Electricity() {
		private Electricity getelecR() {
			return owner().getAtSide(getRotation().R(), posX(), posY()).getElectricalConnection(getRotation().L());
		}
		@Override
		public double insert(double amt, VoltageTier volt) {
			Electricity elec = getelecR();
			if(elec == null) return 0;
			double tfd = elec.insert(amt, volt);
			counter += tfd * volt.volts;
			return tfd;
		}
		@Override
		public VoltageTier voltage() {
			return VoltageTier.V9;
		}
		@Override
		public double pressure() {
			Electricity elec = getelecR();
			if(elec == null) return 0;
			return elec.pressure();
		}
		@Override
		public double pressureWeight() {
			Electricity elec = getelecR();
			if(elec == null) return 0;
			return elec.pressure();
		}
		@Override
		public double extract(double amt, VoltageTier volt, Runnable blow) {
			return 0;
		}
	};
	private final Electricity OUT = new Electricity() {
		private Electricity getelecL() {
			return owner().getAtSide(getRotation().L(), posX(), posY()).getElectricalConnection(getRotation().R());
		}
		@Override
		public double insert(double amt, VoltageTier volt) {
			return 0;
		}
		@Override
		public double extract(double amt, VoltageTier volt, Runnable blow) {
			Electricity elec = getelecL();
			if(elec == null) return 0;
			double ex = elec.extract(amt, volt, blow);
			counter += ex*volt.volts;
			return ex;
		}

		@Override
		public VoltageTier voltage() {
			return VoltageTier.V9;
		}

		@Override
		public double pressure() {
			Electricity elec = getelecL();
			if(elec == null) return 0;
			return elec.pressure();
		}

		@Override
		public double pressureWeight() {
			Electricity elec = getelecL();
			if(elec == null) return 0;
			return elec.pressureWeight();
		}
		
	};
	@Override
	public Electricity getElectricalConnection(Side s) {
		if(s == getRotation().L()) return IN;
		if(s == getRotation().R()) return OUT;
		return null;
	}
	
	//Block methods
	@Override
	public BlockType type() {
		return ContentsBlocks.PMETER;
	}
	private static final RotatedImageGroup rig = RotatedImageGroup.create("machine/power/pmeter.png");
	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}
	@Override
	public void debug(StringBuilder sb) {
		sb.append(label).append('\n');
	}
	@Override
	public void onTick(MapProxy map) {
		//Measure power throughput
		label = UnitFormatter.formatPower(counter);
		counter = 0;
	}
	@Override
	public void render(int x, int y, Graphics g, int side) {
		super.render(x, y, g, side);
		g.setColor(Color.BLACK);
		g.drawString(label, x+4, y+20);
	}
	@Override
	public BlockEntry blockCopy() {
		PowerMeter copy = new PowerMeter();
		copy.setRotation(getRotation());
		return copy;
	}
}
