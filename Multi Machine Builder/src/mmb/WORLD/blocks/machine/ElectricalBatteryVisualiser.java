/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.Graphics;

import javax.swing.JProgressBar;

import mmb.WORLD.electric.Battery;

/**
 * @author oskar
 *
 */
public class ElectricalBatteryVisualiser extends JProgressBar {
	private static final long serialVersionUID = -822042501287883479L;
	private Battery battery;
	/** @return battery currently visualised */
	public Battery getBattery() {
		return battery;
	}
	/** @param battery new battery to visualise */
	public void setBattery(Battery battery) {
		this.battery = battery;
		repaint();
	}
	@Override
	public void paint(Graphics g) {
		if(battery != null) {
			setMaximum((int) battery.capacity());
			setMinimum(0);
			setValue((int) battery.amt);
		}
		super.paint(g);
	}

}
