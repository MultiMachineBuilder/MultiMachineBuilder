/**
 * 
 */
package mmb.MENU;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.MENU.menus.Menus;
import mmb.MENU.toolkit.UIBasis;
import mmb.MENU.toolkit.components.UIButton;
import mmb.MENU.toolkit.components.UIFiller;
import mmb.MENU.toolkit.components.UILayoutLinear;
import mmb.MENU.toolkit.components.UILayoutLinear.Axis;
import mmb.MENU.toolkit.components.UIMenuNavigator;
import mmb.MENU.toolkit.components.UISliderLimited;
import mmb.MENU.toolkit.components.UITextScrollist;
import mmb.MENU.toolkit.menus.Menu;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class GameWindow extends JFrame {
	private static Debugger debug = new Debugger("MENU TESTING");
	private JPanel contentPane;
	public static UIMenuNavigator uimn;
	/**
	 * Launch the application.
	 */
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GameWindow frame = new GameWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		uimn = new UIMenuNavigator();
		uimn.setMenu(Menus.mainMenu);
		
		UIBasis panel = new UIBasis(uimn);
		contentPane.add(panel, BorderLayout.CENTER);
		
		panel.setDynamic(true);
	}

}
