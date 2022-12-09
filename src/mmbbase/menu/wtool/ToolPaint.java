/**
 * 
 */
package mmbbase.menu.wtool;

import static mmb.engine.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.engine.block.BlockEntry;
import mmb.engine.java2d.MappedColorTexture;
import mmb.engine.texture.Textures;
import mmbbase.beans.Colorable;
import mmbbase.menu.world.ColorGUI;
import mmbbase.menu.world.window.WorldWindow;

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
			ask4color("Paint color", c, cc -> c = cc, window);
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

	private final String title = $res("paint");
	@Override
	public String title() {
		return title;
	}

	@Override
	public Icon getIcon() {
		return textureIcon;
	}

	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		// unused
	}

	private final String descr = $res("paint-descr");
	@Override
	public String description() {
		return descr;
	}
	
	/**
	 * Asks for a paint color
	 * @param message the message
	 * @param initial the initial color
	 * @param action this will run after the color is confirmed
	 * @param window world window to open the question in
	 */
	public static void ask4color(String message, Color initial, Consumer<Color> action, WorldWindow window) {
		ColorGUI gui = new ColorGUI(initial, action, window);
		window.openAndShowWindow(gui, message);
	}
}
