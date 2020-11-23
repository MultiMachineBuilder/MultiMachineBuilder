/**
 * 
 */
package mmb.ui.game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.WORLD.inventory.InvGUI.InvGUI;
import mmb.WORLD.player.DataLayerPlayer;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class InventoryFrame extends JFrame {
	private static Debugger debug = new Debugger("INVENTORY");
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public InventoryFrame(DataLayerPlayer pdata) {
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		InvGUI panel = new InvGUI(pdata);//OK
		contentPane.add(panel, BorderLayout.CENTER);
	}

}
