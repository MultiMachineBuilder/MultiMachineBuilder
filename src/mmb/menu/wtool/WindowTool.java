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

import mmb.NN;
import mmb.beans.Titled;
import mmb.menu.world.window.WorldFrame;
import mmb.menu.world.window.WorldWindow;
import monniasza.collects.Identifiable;

/**
 * <br>The {@code WindowTool} abstract class describes a world tool, which applies its code to the world frame.
 * <br>The tool supports {@link MouseListener}, {@link MouseMotionListener}, {@link MouseWheelListener} and {@link KeyListener},
 * <br>which are delegated from the world frame to the tool. Each registered tool is created once per created world window.
 * <br>The tool handles events from the world window and performs appropriate actions.
 * 
 * Events:
 * windowClosed
 * selected
 * deselected
 * @author oskar
 */
public abstract class WindowTool implements
MouseListener,
MouseMotionListener,
MouseWheelListener,
KeyListener,
Identifiable<String>,
Titled{	
	//Declaration
	/** Tool ID */
	@NN public final String id;
	@Override
	public final String id() {
		return id;
	}
	/**
	 * Creates a new window tool
	 * @param s canonical identifier, same as the one declared when the window tool is registered
	 */
	protected WindowTool(String s) {
		this.id = s;
	}
	
	//Key listeners
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
	
	//Mouse listeners
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
	public void mouseClicked(MouseEvent e) {
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
	public void mousePressed(MouseEvent e) {
		//to be implemented by its user
	}
	@Override
	public void mouseReleased(@SuppressWarnings("null") MouseEvent e) {
		//to be implemented by its user
	}
	
	//Control listeners
	/**
	 * Invoked when window is closed
	 * @param window1 the window, which is closed
	 * @param frame1 the associated world frame
	 */
	public void windowClosed(WorldWindow window1, WorldFrame frame1) {
		//to be implemented by its user
	}
	/** Invoked when tool is selected */
	public void selected() {
		//to be implemented by its user
	}
	/** Invoked when tool is deselected */
	public void deselected() {
		//to be implemented by its user
	}

	/** @return the GUI of the block tool, displayed on the lower right pane */
	public Component GUI() {
		return null;
	}
	protected WorldWindow window;
	protected WorldFrame frame;
	/**
	 * Replaces the world window. This method should not be called by mods
	 * @param window
	 */
	public void setWindow(WorldWindow window) {
		this.window = window;
		frame = window.getWorldFrame();
	}
	
	//UI
	/**
	 * @return the description of the window tool;
	 */
	public String description() {
		return ""; //NOSONAR the description can be overridden
	}
	/** @return the icon of the tool */
	public abstract Icon getIcon();
	/**
	 * @param startX left X coordinate
	 * @param startY upper Y coordinate
	 * @param blockScale display scale
	 * @param g graphics context
	 */
	public abstract void preview(int startX, int startY, double blockScale, Graphics g);
}
