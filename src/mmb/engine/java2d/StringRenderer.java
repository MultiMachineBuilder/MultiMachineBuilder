/**
 * 
 */
package mmb.engine.java2d;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Objects;
import java.util.Scanner;

import javax.annotation.Nullable;

/**
 * @author oskar
 *
 */
public class StringRenderer {
	/**
	 * @param bound color of bounds, may be null
	 * @param bg
	 * @param text
	 * @param string the text to draw
	 * @param x X position of the box
	 * @param y Y position of the box
	 * @param margX horizontal margin
	 * @param margY vertical margin
	 * @param g graphics context
	 */
	public static void renderStringBounded(@Nullable Color bound, @Nullable Color bg, Color text,
			String string, int x, int y, int margX, int margY, Graphics g) {
		Objects.requireNonNull(text, "text color is null");
		Objects.requireNonNull(string, "text is null");
		Objects.requireNonNull(g, "graphics is null");
		FontMetrics fm = g.getFontMetrics();
		int w = 0;
		int lines = (int) string.chars().filter(c -> c == '\n').count();
		try(Scanner scanner = new Scanner(string)){	
			while(scanner.hasNextLine()) {
				String next = scanner.nextLine();
				int currW = fm.stringWidth(next);
				if(currW > w) w = currW;
			}
		}
		int h = ((lines - 1) * fm.getHeight());
		int totalH = h + margY + margY + fm.getAscent();
		int totalW = w + margX + margX;
		
		//Render
		if(bg != null) {
			g.setColor(bg);
			g.fillRect(x, y, totalW, totalH);
		}
		if(bound != null) {
			g.setColor(bound);
			g.drawRect(x, y, totalW, totalH);
		}
		g.setColor(text);
		renderMultilineString(string, x+margX, y+margY+fm.getAscent(), fm.getHeight(), g);
	}
	/**
	 * @param s
	 * @param x
	 * @param y
	 * @param offset offset between lines
	 */
	public static void renderMultilineString(String s, int x, int y, int offset, Graphics g) {
		try(Scanner scanner = new Scanner(s)){
			int currY = y;
			while(scanner.hasNextLine()) {
				String next = scanner.nextLine();
				g.drawString(next, x, currY);
				currY += offset;
			}
		}
	}
}
