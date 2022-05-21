/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.vecmath.Point2d;

import org.joml.Vector2d;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.visuals.VisCircle;
import mmb.WORLD.visuals.VisImgRect;
import mmb.WORLD.visuals.VisLine;
import mmb.WORLD.visuals.VisPoint;
import mmb.WORLD.visuals.VisRect;
import mmb.WORLD.visuals.Visual;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ToolVisuals extends WindowTool{
	public ToolVisuals() {
		super("visuals");
	}
	private static final Debugger debug = new Debugger("TOOL VISUALS");

	@Override
	public String title() {
		return "Paint visuals";
	}

	@Nonnull public static final Icon ICON = new ImageIcon(Textures.get("visuals.png"));
	@Override
	public Icon getIcon() {
		return ICON;
	}

	
	//Tool parameters
	@Nonnull Vector2d p1 = new Vector2d();
	@Nonnull Vector2d p2 = new Vector2d();
	double r;
	Vector2d pointingAt = new Vector2d();
	Color border; //Border color
	Color fill; //Fill color
	Visual previewer; //Preview object
	int geomType; //0 - point, 1-image, 2-circle, 3-rectangle, 4-image
	int posMode;
	boolean autorefresh;
	
	@Override
	public void preview(int startX, int startY, int scale, Graphics g) {
		recalc();
		autorefresh = GUI.autoRefresh.isSelected();
		debug.printl("autorefresh "+autorefresh);
		if(previewer != null) previewer.render(g, frame);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_O:
			//Border color
			ToolPaint.ask4color("Border color", border  == null?Color.WHITE:border, c -> border=c, window);
			break;
		case KeyEvent.VK_P:
			//Fill color
			ToolPaint.ask4color("Fill color", fill == null?Color.WHITE:fill, c -> fill=c, window);
			break;
		case KeyEvent.VK_I:
			//Confirm position
			if(posMode == 1) {
				//pos1
				p1.set(pointingAt);
			}else if(posMode == 2){
				//pos2
				p2.set(pointingAt);
			}
			posMode=0;
			break;
		case KeyEvent.VK_J:
			//pos1
			posMode = 1;
			break;
		case KeyEvent.VK_K:
			//pos1
			posMode = 2;
			break;
		case KeyEvent.VK_L:
			recalc();
			if(previewer != null) frame.getMap().addVisual(previewer);
			break;
		case KeyEvent.VK_Z:
			geomType = 0;
			break;
		case KeyEvent.VK_X:
			geomType = 1;
			break;
		case KeyEvent.VK_C:
			geomType = 2;
			break;
		case KeyEvent.VK_V:
			geomType = 3;
			break;
		case KeyEvent.VK_B:
			geomType = 4;
			break;
		case KeyEvent.VK_N:
			border = null;
			break;
		case KeyEvent.VK_M:
			border = null;
			break;
		}
	}

	private void recalc() {
		switch(geomType) {
		case 0:
			//point
			if(border == null) previewer = null;
			else previewer = new VisPoint(p1.x, p1.y, border);
			break;
		case 1:
			//line
			if(border == null) previewer = null;
			else previewer = new VisLine(p1.x, p1.y, p2.x, p2.y, border);
			break;
		case 2:
			//circle
			r = p1.distance(p2);
			previewer = new VisCircle(p1.x, p1.y, r, fill, border);
			break;
		case 3:
			//rectangle
			if(p1.x > p2.x || p1.y > p2.y) previewer = null;
			else previewer = new VisRect(p1.x, p1.y, p2.x, p2.y, fill, border);
			break;
		default:
			//image
			ItemRecord item = window.getPlacer().getSelectedValue();
			if(item == null || p1.x > p2.x || p1.y > p2.y) previewer = null;
			else previewer = new VisImgRect(p1.x, p1.y, p2.x, p2.y, ItemEntry.drawer(item.item()));
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		frame.worldAt(e.getX(), e.getY(), pointingAt);
		if(autorefresh) {
			if(posMode == 1) {
				//pos1
				p1.set(pointingAt);
			}else if(posMode == 2){
				//pos2
				p2.set(pointingAt);
			}
		}
	}

	private final ToolVisualsPanel GUI = new ToolVisualsPanel();
	@Override
	public Component GUI() {
		return GUI;
	}

}
