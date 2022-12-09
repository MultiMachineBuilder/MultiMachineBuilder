/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Color;
import java.awt.Graphics;

import mmb.content.ContentsBlocks;
import mmb.content.electric.Electricity;
import mmb.content.electric.VoltageTier;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class PowerMeter extends BlockEntityRotary {
	@Override
	public void debug(StringBuilder sb) {
		sb.append(label).append('\n');
	}

	private final Electricity IN = new Electricity() {
		@Override
		public double insert(double amt, VoltageTier volt) {
			Electricity elec = getelec();
			if(elec == null) return 0;
			double tfd = elec.insert(amt, volt);
			moved += tfd * volt.volts;
			return tfd;
		}
		@Override
		public VoltageTier voltage() {
			return VoltageTier.V9;
		}
		@Override
		public double pressure() {
			Electricity elec = getelec();
			if(elec == null) return 0;
			return elec.pressure();
		}
		@Override
		public double pressureWeight() {
			Electricity elec = getelec();
			if(elec == null) return 0;
			return elec.pressure();
		}
		@Override
		public double extract(double amt, VoltageTier volt, Runnable blow) {
			return 0;
		}
	};
	private final Electricity OUT = new Electricity() {
		@Override
		public double insert(double amt, VoltageTier volt) {
			return 0;
		}
		@Override
		public double extract(double amt, VoltageTier volt, Runnable blow) {
			Electricity elec = getelec();
			if(elec == null) return 0;
			double ex = elec.extract(amt, volt, blow);
			moved += ex*volt.volts;
			return ex;
		}

		@Override
		public VoltageTier voltage() {
			return VoltageTier.V9;
		}

		@Override
		public double pressure() {
			Electricity elec = getelec();
			if(elec == null) return 0;
			return elec.pressure();
		}

		@Override
		public double pressureWeight() {
			Electricity elec = getelec();
			if(elec == null) return 0;
			return elec.pressureWeight();
		}
		
	};
	private Electricity getelec() {
		return owner().getAtSide(getRotation().L(), posX(), posY()).getElectricalConnection(getRotation().R());
	}
	@Override
	public Electricity getElectricalConnection(Side s) {
		if(s == getRotation().L()) return IN;
		if(s == getRotation().R()) return OUT;
		return null;
	}

	private static final RotatedImageGroup rig = RotatedImageGroup.create("machine/power/pmeter.png");
	
	private double moved;
	private String label;
	@Override
	public void onTick(MapProxy map) {
		//Measure power throughput
		label = formatOut(moved * 50);
		moved = 0;
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.PMETER;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	public void render(int x, int y, Graphics g, int side) {
		super.render(x, y, g, side);
		g.setColor(Color.BLACK);
		g.drawString(label, x+4, y+20);
	}

	public static String formatOut(double pwr) {
		if(pwr > 9_999_999) {
			//Megawatts
			return ((int)(pwr / 10_000)) / 100.0 + "㎿";
		}
		if(pwr > 9999.999) {
			//Kilowatts
			return ((int)(pwr / 10)) / 100.0 + "㎾";
		}
		if(pwr > 9.999999) {
			//Watts
			return ((int)(pwr * 100)) / 100.0 + "W";
		}
		if(pwr > 9.999999) {
			//MiliWatts
			return ((int)(pwr * 100_000)) / 100.0 + "㎽";
		}
		if(pwr > 9.999999) {
			//MicroWatts
			return ((int)(pwr * 100_000)) / 100.0 + "㎼";
		}
		if(pwr > 9.999999) {
			//NanoWatts
			return ((int)(pwr * 100_000_000)) / 100.0 + "㎻";
		}
			//PicoWatts
			return ((int)(pwr * 100_000_000_000.0)) / 100.0 + "㎺";
		
	}
	@Override
	public BlockEntry blockCopy() {
		PowerMeter copy = new PowerMeter();
		copy.setRotation(getRotation());
		return copy;
	}
}
