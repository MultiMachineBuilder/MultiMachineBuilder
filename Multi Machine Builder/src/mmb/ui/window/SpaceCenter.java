/**
 * 
 */
package mmb.ui.window;

import javax.swing.*;
import java.awt.*;


/**
 * @author oskar
 *
 */
public class SpaceCenter extends JFrame {
	public static void main(String[] args) {
		SpaceCenter frame = new SpaceCenter();
		frame.setVisible(true);
	}
	/**
	 * 
	 */
	public SpaceCenter() {
		super("Space Center");
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(3);
		setSize(new Dimension(100, 100));
		setLocation(new Point(100, 50));
		setBounds(100, 50, 100 ,100);
		initComponents();
		//setResizable(false);
		centerPack();
	}
		
	JButton centrum, up, down, left, right;
	
	public void initComponents() {
		centrum = new JButton("Hello");
		up = new JButton("^");
		down = new JButton("v");
		left = new JButton("<");
		right = new JButton(">");
		
		Container container = this.getContentPane();
		
		container.add(centrum, BorderLayout.CENTER);
		container.add(centrum, BorderLayout.PAGE_START);
		container.add(centrum, BorderLayout.PAGE_END);
		container.add(centrum, BorderLayout.LINE_START);
		container.add(centrum, BorderLayout.LINE_END);
	}
	
	public void center(int x, int y) {
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(x, y);
		this.setLocation((res.width-x)/2, (res.height-y)/2);
	}
	
	public void centerPack() {
		center(getWidth(), getHeight());
		pack();
		center(getWidth(), getHeight());
	}
}
