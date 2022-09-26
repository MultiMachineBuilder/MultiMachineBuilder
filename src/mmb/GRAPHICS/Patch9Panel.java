/**
 * 
 */
package mmb.GRAPHICS;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author oskar
 *
 */
public class Patch9Panel extends JPanel {
	private Patch9Image image;
	
	public Patch9Image getImage() {
		return image;
	}
	public void setImage(Patch9Image image) {
		this.image = image;
	}
	public void setImage(BufferedImage image) {
		this.image = new Patch9Image(image);
	}

	public Patch9Panel() {
		super();
	}
	public Patch9Panel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}
	public Patch9Panel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}
	public Patch9Panel(LayoutManager layout) {
		super(layout);
	}

	@Override
	public void paint(Graphics arg0) {
		if(image == null) {
			//Draw as normal JPanel
			super.paint(arg0);
		}else {
			//Draw the background
			image.draw(arg0, 0, 0, getWidth(), getHeight());
			paintChildren(arg0);
		}
	}

}
