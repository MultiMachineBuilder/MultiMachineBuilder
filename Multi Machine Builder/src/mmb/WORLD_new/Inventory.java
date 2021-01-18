/**
 * 
 */
package mmb.WORLD_new;

import java.awt.EventQueue;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.List;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JTextField;

import mmb.MENU.ImageTextList;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;

/**
 * @author oskar
 *
 */
public class Inventory extends JDialog {
	private static final long serialVersionUID = -5196828235101928910L;
	private JLabel lblNewLabel;
	private JButton btn1L;
	private JButton btnL;
	private JButton btnStackL;
	private JButton btnAllL;
	private JButton btnAllR;
	private JButton btnStackR;
	private JButton btn1R;
	private JButton btnR;
	private JTextField txtSearchItemsL;
	private ImageTextList listPlayer;
	private JTextField txtAmount;
	private JLabel lblNewLabel_1;
	private JTextField txtSearchItemsR;
	private JMenuBar menuBar;
	private ImageTextList listInv;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inventory dialog = new Inventory();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public Inventory() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow][grow][][][][][]"));
		
		lblNewLabel = new JLabel("Player");
		getContentPane().add(lblNewLabel, "cell 0 0");
		
		btn1L = new JButton("< 1x");
		getContentPane().add(btn1L, "flowx,cell 1 0,growx");
		
		lblNewLabel_1 = new JLabel("Inventory");
		getContentPane().add(lblNewLabel_1, "cell 2 0");
		
		listPlayer = new ImageTextList();
		getContentPane().add(listPlayer, "cell 0 1 1 5,grow");
		
		btnL = new JButton("<<all of these");
		getContentPane().add(btnL, "cell 1 1,growx,aligny center");
		
		listInv = new ImageTextList();
		getContentPane().add(listInv, "cell 2 1 1 5,grow");
		
		btnAllL = new JButton("<<< entire inventory");
		getContentPane().add(btnAllL, "cell 1 2,growx,aligny center");
		
		btnAllR = new JButton("entire inventory >>>");
		getContentPane().add(btnAllR, "cell 1 3,growx,aligny center");
		
		btnStackR = new JButton("all of these >>");
		getContentPane().add(btnStackR, "cell 1 4,growx,aligny center");
		
		btn1R = new JButton("1x >");
		getContentPane().add(btn1R, "flowx,cell 1 5,growx,aligny center");
		
		btnStackL = new JButton("< given amount");
		getContentPane().add(btnStackL, "cell 1 0,growx");
		
		btnR = new JButton("given amount >");
		getContentPane().add(btnR, "cell 1 5,growx,aligny center");
		
		lblNewLabel_2 = new JLabel("Search player's items");
		getContentPane().add(lblNewLabel_2, "cell 0 6");
		
		lblNewLabel_3 = new JLabel("Number of items");
		getContentPane().add(lblNewLabel_3, "cell 1 6");
		
		lblNewLabel_4 = new JLabel("Search inventory items");
		getContentPane().add(lblNewLabel_4, "cell 2 6");
		
		txtSearchItemsL = new JTextField();
		txtSearchItemsL.setToolTipText("Search items");
		getContentPane().add(txtSearchItemsL, "cell 0 7,growx");
		txtSearchItemsL.setColumns(10);
		
		txtAmount = new JTextField();
		txtAmount.setToolTipText("Number of items");
		getContentPane().add(txtAmount, "cell 1 7,growx");
		txtAmount.setColumns(10);
		
		txtSearchItemsR = new JTextField();
		txtSearchItemsR.setToolTipText("Search items");
		getContentPane().add(txtSearchItemsR, "cell 2 7,growx");
		txtSearchItemsR.setColumns(10);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

	}

}
