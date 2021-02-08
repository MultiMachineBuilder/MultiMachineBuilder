/**
 * 
 */
package mmb.testing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.WORLD.block.Rotations;

/**
 * @author oskar
 *
 */
public class TestImageTransform extends JFrame {

	private JPanel contentPane;
	private TestRotationsDrawer testRotationsDrawer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestImageTransform frame = new TestImageTransform();
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
	public TestImageTransform() {
		initialize();
	}
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		BufferedImage img;
		try {
			img = ImageIO.read(new File("textures/example rotation.png"));
			BufferedImage[] images = Rotations.mirroredVersions(img);
			BufferedImage result = new BufferedImage(256, 32, BufferedImage.TYPE_INT_RGB);
			testRotationsDrawer = new TestRotationsDrawer();
			testRotationsDrawer.images = images;
			contentPane.add(testRotationsDrawer, BorderLayout.CENTER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
