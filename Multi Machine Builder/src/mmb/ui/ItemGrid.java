/**
 * 
 */
package mmb.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mmb.items.Item;

/**
 * @author oskar
 *
 */
public class ItemGrid extends JPanel implements List2D<Item> {
	public Dimension gridSize;
	private Item[] items;
	/**
	 * Create the panel.
	 */
	public ItemGrid() {

	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(gridSize.width * 32, gridSize.width * 32);
	}
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	@Override
	public void repaint() {
		Graphics2D g = (Graphics2D) getGraphics();
		int x = 0;
		int z = 0;
		for(int i = 0; i < gridSize.width; i++) {
			int y = 0;
			for(int j = 0; j < gridSize.height; j++) {
				paintItem(items[z], x, y, g);
				y += 32;
				z++;
			}
			x += 32;
		}
		super.repaint();
	}
	
	private void paintItem(Item item, int x, int y, Graphics2D g) {
		if(item != null) {
			BufferedImage visual = item.getVisual();
			if(visual == null) {
				//No visual
			}else {
				g.drawImage(visual, x, y, null);
			}
		}
	}
	

}
