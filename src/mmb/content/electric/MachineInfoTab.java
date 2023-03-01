/**
 * 
 */
package mmb.content.electric;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import mmb.content.electric.machines.ElectroMachine;
import mmb.menu.world.craft.*;

/**
 * A GUI component which provides common machine information in the GUI
 * @author oskar
 */
public class MachineInfoTab extends Box {
	private static final long serialVersionUID = -7855159998857476643L;

	public MachineInfoTab(ElectroMachine machine) {
		super(BoxLayout.Y_AXIS);
		
		JLabel volt = new JLabel(CRConstants.VOLT+machine.voltage().name);
		add(volt);
		
		JLabel title = new JLabel(machine.machineName());
		add(title);
		
		JCheckBox autoextract = new JCheckBox(CRConstants.AUTO, machine.isAutoExtract());
		autoextract.addActionListener(e -> machine.setAutoExtract(autoextract.isSelected()));
		add(autoextract);
		
		JCheckBox pass = new JCheckBox(CRConstants.PASS, machine.isPass());
		pass.addActionListener(e -> machine.setPass(pass.isSelected()));
		add(pass);
	}
}
