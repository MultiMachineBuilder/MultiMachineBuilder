/**
 * 
 */
package mmb.WORLD.electromachine;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 * @author oskar
 *
 */
public class MachineInfoTab extends Box {
	public MachineInfoTab(ElectroMachine machine) {
		super(BoxLayout.Y_AXIS);
		
		JLabel volt = new JLabel("Voltage tier: "+machine.voltage().name);
		add(volt);
		
		JLabel title = new JLabel(machine.machineName());
		add(title);
		
		JCheckBox autoextract = new JCheckBox("Auto-extract", machine.isAutoExtract());
		autoextract.addActionListener(e -> machine.setAutoExtract(autoextract.isSelected()));
		add(autoextract);
		
		JCheckBox pass = new JCheckBox("Pass on unsupported items", machine.isPass());
		pass.addActionListener(e -> machine.setPass(pass.isSelected()));
		add(pass);
	}
}
