/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.Nonnull;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class PowerMeter extends SkeletalBlockEntityRotary {
	@Override
	public void debug(StringBuilder sb) {
		sb.append(label).append('\n');
	}

	private final Electricity IN = new Electricity() {
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
			Electricity elec = owner.getAtSide(side.R(), x, y).getElectricalConnection(side.L());
			if(elec == null) return 0;
			double tfd = elec.insert(amt);
			moved += tfd;
			//debug.printl(Double.toString(tfd));
			return tfd;
		}
	};
	@Override
	public Electricity getElectricalConnection(Side s) {
		if(s == side.L()) return IN;
		if(s == side.R()) return Electricity.NONE;
		return null;
	}

	private static final RotatedImageGroup rig = RotatedImageGroup.create("machine/power/pmeter.png");
	public PowerMeter(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2);
	}
	
	private double moved;
	private String label;
	private static final Debugger debug = new Debugger("POWER METER");
	@Override
	public void onTick(MapProxy map) {
		//Measure power throughput
		label = formatOut(moved * 50);
		//debug.printl(label);
		//debug.printl(Double.toString(moved));
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
	public void render(int x, int y, Graphics g) {
		super.render(x, y, g);
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
}
