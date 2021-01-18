/**
 * 
 */
package mmb.testing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.Patch9Panel;

/**
 * @author oskar
 *
 */
public class Patch9Test extends JFrame {

	private JPanel contentPane;
	private Patch9Panel patch9Panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Patch9Test frame = new Patch9Test();
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
	public Patch9Test() {
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		patch9Panel = new Patch9Panel();
		contentPane.add(patch9Panel, BorderLayout.CENTER);
		patch9Panel.setImage(Textures.get("UIs/blue signpost.png"));
	}

}
