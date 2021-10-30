/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Color;
import java.awt.Dimension;

import javax.annotation.Nullable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.BlockEntityData;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class PowerLoad extends BlockEntityData implements BlockActivateListener {
	private double power;
	@Override
	public Electricity getElectricalConnection(Side s) {
		return elec;
	}

	private double accumulated = 0;
	private static class Elec implements Electricity{
		private final PowerLoad load;
		private Elec(PowerLoad load) {
			this.load = load;
		}
		@Override
		public double amount() {
			return 0;
		}
		@Override
		public double capacity() {
			return 0;
		}
		@Override
		public double insert(double amt) {
			double remain = (load.power * 0.02) - load.accumulated;
			if(remain < 0) return 0;
			double result = Math.min(amt, remain);
			load.accumulated += result;
			
			return result;
		}	
	}
	private Electricity elec = new Elec(this);

	@Override
	public BlockType type() {
		return ContentsBlocks.LOAD;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		setPower(data.get("power").asDouble());
	}

	@Override
	protected void save0(ObjectNode node) {
		node.put("power", power);
	}

	/**
	 * @return current power limit in watts
	 */
	public double getPower() {
		return power;
	}

	/**
	 * @param power new power limit in watts
	 * @throws IllegalArgumentException if power is negative or NaN
	 */
	public void setPower(double power) {
		if(Double.isNaN(power)) throw new IllegalArgumentException("The power limit is NaN");
		if(power < 0) throw new IllegalArgumentException("The power limit is negative");
		this.power = power;
	}

	@Override
	public void onTick(MapProxy map) {
		accumulated = 0;
	}
	
	private class Dialog extends JPanel{
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
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openAndShowWindow(new Dialog(window), "Power load");
	}

	@Override
	public PowerLoad clone() {
		PowerLoad copy = (PowerLoad) super.clone();
		copy.elec = new Elec(copy);
		return copy;
	}

}
