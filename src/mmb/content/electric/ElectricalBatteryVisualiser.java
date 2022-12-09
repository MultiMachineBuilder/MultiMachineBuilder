/**
 * 
 */
package mmb.content.electric;

import java.awt.Graphics;

import javax.swing.JProgressBar;

/**
 * @author oskar
 *
 */
public class ElectricalBatteryVisualiser extends JProgressBar {
	public ElectricalBatteryVisualiser() {
	}
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
	public void paint(@SuppressWarnings("null") Graphics g) {
		if(battery != null) {
			setMaximum((int) battery.capacity);
			setMinimum(0);
			setValue((int) battery.stored);
		}
		super.paint(g);
	}

}
