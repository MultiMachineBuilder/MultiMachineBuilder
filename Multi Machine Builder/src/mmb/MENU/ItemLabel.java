/**
 * 
 */
package mmb.MENU;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import mmb.WORLD.BlockDrawer;
import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class ItemLabel extends JLabel {
	public ItemLabel() {
		super();
	}
	public ItemLabel(ItemType type) {
		super();
		setTexture(type.drawer);
	}
	public ItemLabel(BlockDrawer drawer) {
		super();
		setTexture(drawer);
	}
	public ItemLabel(BlockDrawer drawer, String s) {
		super(s);
		setTexture(drawer);
	}
	public ItemLabel(String s) {
		super(s);
	}
	private BlockDrawer texture;
	private static final long serialVersionUID = -5600547352358346130L;
	private int preferredW = -1, preferredH = -1;
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(preferredW, preferredH);
	}

	@Override
	public void setText(String s) {
		super.setText(s);
		resetSize(s);
	}

	/**
	 * @return the texture
	 */
	public BlockDrawer getTexture() {
		return texture;
	}

	@Override
	public void paint(Graphics g) {
		if(preferredW < 0 || preferredH < 0) resetSize(getText());
		Rectangle rect = g.getClipBounds();
		int textHeight = g.getFontMetrics().getHeight();
		int textpos = (rect.height - textHeight)/2;
		texture.draw(4, 4, g);
		g.drawString(getText(), 40, textpos);
	}

	/**
	 * 
	 */
	private void resetSize(String s) {
		Font f = getFont();
		if(f == null) {
			f = new JLabel().getFont();
			setFont(f);
		}
		FontMetrics fm = getFontMetrics(f);
		if(fm == null) return;
		int txtWidth = fm.stringWidth(s);
		preferredW = 40+txtWidth;
		preferredH = 40;
	}
	/**
	 * Set the texture used in the label
	 * @param texture the texture to set
	 */
	public void setTexture(BlockDrawer texture) {
		this.texture = texture;
	}

}
