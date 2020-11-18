/**
 * 
 */
package mmb.WORLD.tileworld;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import mmb.WORLD.tileworld.block.Block;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author oskar
 *
 */
public class BlockPalette extends JPanel {
	private Block[] blocks;
	/**
	 * Create the panel.
	 */
	public BlockPalette() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				Point pos  = arg0.getPoint();
				if(pos.x > 31) { //select tool
					
				}else { //select block
					
				}
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(64, super.getPreferredSize().height);
	}
	public void paint(Graphics g) {
		//Paint blocks
		
		
		//Paint tools
	}

}
