/**
 * 
 */
package mmb.WORLD.gui;

import javax.swing.JPanel;

import mmb.WORLD.gui.window.WorldWindow;

import javax.swing.JColorChooser;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class ColorGUI extends JPanel {
	private static final long serialVersionUID = 5195216057761670896L;
	
	private JColorChooser colorChooser;
	private JButton btnNewButton;
	private JButton btnNewButton_1;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("null")
	public ColorGUI(Variable<Color> c, WorldWindow win) {
		setLayout(new MigLayout("", "[fill]", "[450px][21px][21px]"));
		
		colorChooser = new JColorChooser();
		add(colorChooser, "cell 0 0,grow,aligny center");
		colorChooser.setColor(c.get());
		
		btnNewButton = new JButton("OK");
		btnNewButton.setBackground(Color.GREEN);
		btnNewButton.addActionListener(e -> {
			c.set(colorChooser.getColor());
			win.closeWindow(this);
		});
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnNewButton, "cell 0 1,growx,aligny center");
		
		btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBackground(Color.RED);
		btnNewButton_1.addActionListener(e -> win.closeWindow(this));
		btnNewButton_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnNewButton_1, "cell 0 2,growx,aligny center");
	}

}
