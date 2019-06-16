package testing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import debug.Avaliability;
import debug.AvaliabilityError;

import java.awt.BorderLayout;

public class TestFormat {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFormat window = new TestFormat();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}); 
	}

	/**
	 * Create the application.
	 */
	public TestFormat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblOperativeMod = new JLabel("Operative mod");
		lblOperativeMod.setBounds(10, 0, 434, 14);
		frame.getContentPane().add(lblOperativeMod);
		
		JLabel lblInoperativeMod = new JLabel("Inoperative mod");
		lblInoperativeMod.setBounds(10, 11, 414, 14);
		frame.getContentPane().add(lblInoperativeMod);
		
		Avaliability av1 = new Avaliability(AvaliabilityError.CORRUPT);
		av1.formatLabel("Inoperative mod", lblInoperativeMod);
		av1 = new Avaliability(AvaliabilityError.AVALIABLE);
		av1.formatLabel("Operative mod", lblOperativeMod);
	}
}
