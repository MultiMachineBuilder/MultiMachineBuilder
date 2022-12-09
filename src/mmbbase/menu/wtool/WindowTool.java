/**
 * 
 */
package mmb.menu.wtool;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Icon;

import mmb.beans.Titled;
import mmb.menu.world.window.WorldFrame;
import mmb.menu.world.window.WorldWindow;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 * <br>The {@code Tool} interface describes a world tool, which applies its code to the world frame.
 * <br>The tool supports {@link MouseListener}, {@link MouseMotionListener}, {@link MouseWheelListener} and {@link KeyListener},
 * <br>which are delegated from the world frame to the tool. Each registered tool is created once per created world window.
 * 
 * Events:
 * windowClosed
 * selected
 * deselected
 */
public abstract class WindowTool implements
MouseListener,
MouseMotionListener,
MouseWheelListener,
KeyListener,
Identifiable<String>,
Titled{
	@Override
	public final String id() {
		return id;
	}
	/**
	 * @return the icon of the tool
	 */
	public abstract Icon getIcon();
	/**
	 * Tool ID
	 */
	public final String id;
	protected WindowTool(String s) {
		this.id = s;
	}
	@Override
	public void keyPressed(@SuppressWarnings("null") KeyEvent e) {
		//to be implemented by its user
	}
	@Override
	public void keyReleased(@SuppressWarnings("null") KeyEvent e) {
		//to be implemented by its user
	}
	@Override
	public void keyTyped(@SuppressWarnings("null") KeyEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseWheelMoved(@SuppressWarnings("null") MouseWheelEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseDragged(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseMoved(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseClicked(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseEntered(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseExited(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mousePressed(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseReleased(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	/**
	 * Invoked when window is closed
	 * @param window the window, which is closed
	 * @param frame the associated world frame
	 */
	public void windowClosed(WorldWindow window, WorldFrame frame) {
		//to be implemented by its user
	}
	/**
	 * Invoked when tool is selected
	 */
	public void selected() {
		//to be implemented by its user
	}
	/**
	 * Invoked when tool is deselected
	 */
	public void deselected() {
		//to be implemented by its user
	}

	/**
	 * @return the GUI of the block tool
	 */
	public Component GUI() {
		return null;
	}
	protected WorldWindow window;
	protected WorldFrame frame;
	public void setWindow(WorldWindow window) {
		this.window = window;
		frame = window.getWorldFrame();
	}
	/**
	 * @return the description of the window tool;
	 */
	public String description() {
		return "";
	}
	
	/**
	 * @param startX left X coordinate
	 * @param startY upper Y coordinate
	 * @param blockScale display scale
	 * @param g graphics context
	 */
	public abstract void preview(int startX, int startY, double blockScale, Graphics g);
}
