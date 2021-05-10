/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.BEANS.Colorable;
import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.awt.MappedColorTexture;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.gui.ColorGUI;
import mmb.WORLD.gui.Variable;

/**
 * @author oskar
 * Press C key to change color, and press X to reset
 */
public class ToolPaint extends WindowTool {
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_C:
			//Open color GUI
			ColorGUI gui = new ColorGUI(cvar, window);
			window.openAndShowWindow(gui, "Paint color");
			break;
		case KeyEvent.VK_X:
			setColor(Color.WHITE);
			window.redrawUIs();
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			int x = frame.getMouseoverBlockX();
			int y = frame.getMouseoverBlockY();
			BlockEntry ent = frame.getMap().get(x, y);
			if(ent instanceof Colorable) ((Colorable) ent).setColor(c);
		}
	}


	@Nonnull private static final BufferedImage img = Textures.get("tool/paint.png");
	@SuppressWarnings("null")
	@Nonnull private Color c = Color.WHITE;
	@SuppressWarnings("null")
	private final MappedColorTexture texture = new MappedColorTexture(Color.RED, Color.WHITE, img);
	public static final Icon icon = new ImageIcon(img);
	private final Icon textureIcon = texture.iconRenderer();
	private final Variable<Color> cvar = Variable.delegate(this::getColor, this::setColor);
	/**
	 * @return this paintbrushes current color
	 */
	public Color getColor() {
		return c;
	}

	/**
	 * @param c new color
	 */
	public void setColor(Color c) {
		this.c = c;
		texture.setTo(c);
	}

	public ToolPaint() {
		super("paint");
	}

	@Override
	public String title() {
		return "Paintbrush";
	}

	
	@Override
	public Icon getIcon() {
		return textureIcon;
	}

}
