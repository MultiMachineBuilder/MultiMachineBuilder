/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Color;
import java.awt.Graphics;

import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 * A visual used to test error detection
 */
public class EvilLine extends VisLine {
	public EvilLine(double x1, double y1, double x2, double y2, Color c) {
		super(x1, y1, x2, y2, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		throw new RuntimeException("I'M AN EVIL LINE");
	}
	
}
