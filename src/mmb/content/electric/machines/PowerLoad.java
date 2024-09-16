/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.electric.Electricity;
import mmb.content.electric.VoltageTier;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * Consumes constant power
 * @author oskar
 */
public class PowerLoad extends BlockEntityData implements BlockActivateListener {
	//Configuration
	private double power;
	/**
	 * @return current power limit in joules per tick
	 */
	public double getPower() {
		return power;
	}
	/**
	 * @param power new power limit in joules per tick
	 * @throws IllegalArgumentException if power is negative or NaN
	 */
	public void setPower(double power) {
		if(Double.isNaN(power)) throw new IllegalArgumentException("The power limit is NaN");
		if(power < 0) throw new IllegalArgumentException("The power limit is negative");
		this.power = power;
	}
	
	//Electricity
	private double accumulated = 0;
	private static class Elec implements Electricity{
		private final PowerLoad load;
		private Elec(PowerLoad load) {
			this.load = load;
		}
		@Override
		public double insert(double amt, VoltageTier volt) {
			double pwr = volt.power(amt);
			double remain = load.power - load.accumulated;
			if(remain < 0) return 0;
			double result = Math.min(pwr, remain);
			load.accumulated += result;
			return result/volt.volts;
		}
		@Override
		public VoltageTier voltage() {
			return VoltageTier.V9;
		}
		@Override
		public double pressure() {
			return 0;
		}
		@Override
		public double pressureWeight() {
			return 0;
		}
		@Override
		public double extract(double amt, VoltageTier volt, Runnable blow) {
			return 0;
		}	
	}
	private Electricity elec = new Elec(this);
	@Override
	public Electricity getElectricalConnection(Side s) {
		return elec;
	}
	
	//Block methods
	@Override
	public BlockType type() {
		return ContentsBlocks.LOAD;
	}
	@Override
	public BlockEntry blockCopy() {
		PowerLoad copy = new PowerLoad();
		copy.power = power;
		return copy;
	}
	@Override
	public void onTick(MapProxy map) {
		accumulated = 0;
	}
	
	//GUI
	private class Dialog extends GUITab{
		private static final long serialVersionUID = 4755568394083395990L;
		final JTextField field;
		/**
		 * Creates a power load dialog window
		 * @param window 
		 * @wbp.parser.entryPoint
		 */
		public Dialog(WorldWindow window) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JButton cancel = new JButton("Cancel");
			cancel.setBackground(Color.RED);
			cancel.addActionListener(e -> cancel(window));
			add(cancel);
			
			JButton ok = new JButton("OK");
			ok.setBackground(Color.GREEN);
			ok.addActionListener(e -> OK(window));
			add(ok);
			
			field = new JTextField();
			field.setPreferredSize(new Dimension(128, 20));
			field.setText(Double.toString(power));
			add(field);
		}
		void OK(WorldWindow window) {
			double pwr = Double.parseDouble(field.getText());
			setPower(pwr);
			window.closeWindow(this);
		}
		void cancel(WorldWindow window) {
			window.closeWindow(this);
		}
		@Override
		public void close(WorldWindow window) {
			//unused
		}
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openAndShowWindow(new Dialog(window), "Power load");
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		setPower(data.get("power").asDouble());
	}
	@Override
	protected void save0(ObjectNode node) {
		node.put("power", power);
	}
}
