/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Color;
import java.awt.Dimension;

import javax.annotation.Nonnull;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class PowerLoad extends SkeletalBlockEntityData implements BlockActivateListener {
	private double power;
	@Override
	public Electricity getElectricalConnection(Side s) {
		return elec;
	}

	private double accumulated = 0;
	private final Electricity elec = new Electricity() {
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
			double remain = (power * 0.02) - accumulated;
			if(remain < 0) return 0;
			double result = Math.min(amt, remain);
			accumulated += result;
			
			return result;
		}
		private final Debugger debug = new Debugger("ELEC LOAD");
	};
	public PowerLoad(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.LOAD;
	}

	@Override
	public void load(JsonNode data) {
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
		 * @wbp.parser.entryPoint
		 */
		public Dialog(WorldWindow window) {
			BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
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
	public void click(int blockX, int blockY, World map, WorldWindow window) {
		if(window == null) return;
		window.openAndShowWindow(new Dialog(window), "Power load");
	}

}
