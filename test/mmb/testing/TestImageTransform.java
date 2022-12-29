/**
 * 
 */
package mmb.testing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import mmb.engine.rotate.Rotations;

/**
 * @author oskar
 *
 */
public class TestImageTransform extends JFrame {

	private JPanel contentPane;
	private TestRotationsDrawer testRotationsDrawer;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				TestImageTransform frame = new TestImageTransform();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestImageTransform() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		BufferedImage img;
		try {
			img = ImageIO.read(new File("textures/example rotation.png"));
			BufferedImage[] images = Rotations.mirroredVersions(img);
			testRotationsDrawer = new TestRotationsDrawer();
			testRotationsDrawer.images = images;
			contentPane.add(testRotationsDrawer, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
