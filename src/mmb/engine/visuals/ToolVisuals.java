/**
 * 
 */
package mmb.engine.visuals;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.joml.Vector2d;

import mmb.NN;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.texture.Textures;
import mmbbase.menu.wtool.ToolPaint;
import mmbbase.menu.wtool.ToolVisualsPanel;
import mmbbase.menu.wtool.WindowTool;

/**
 * @author oskar
 *
 */
public class ToolVisuals extends WindowTool{
	public ToolVisuals() {
		super("visuals");
	}
	private static final Debugger debug = new Debugger("TOOL VISUALS");

	private final String title = $res("visuals");
	@Override
	public String title() {
		return title;
	}

	@NN public static final Icon ICON = new ImageIcon(Textures.get("visuals.png"));
	@Override
	public Icon getIcon() {
		return ICON;
	}

	
	//Tool parameters
	@NN Vector2d p1 = new Vector2d();
	@NN Vector2d p2 = new Vector2d();
	double r;
	Vector2d pointingAt = new Vector2d();
	Color border; //Border color
	Color fill; //Fill color
	Visual previewer; //Preview object
	/**0 - point, 1-image, 2-circle, 3-rectangle, 4-image */
	int geomType;
	/**0-none 1-first, 2-second*/
	int posMode;
	boolean autorefresh;
	
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
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
			geomType = 0; //point
			break;
		case KeyEvent.VK_X:
			geomType = 1; //line
			break;
		case KeyEvent.VK_C:
			geomType = 2; //circle
			break;
		case KeyEvent.VK_V:
			geomType = 3; //rect
			break;
		case KeyEvent.VK_B:
			geomType = 4; //image
			break;
		case KeyEvent.VK_N:
			fill = null;
			break;
		case KeyEvent.VK_M:
			border = null;
			break;
		default:
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

	private static final String descr1 = $res("visuals-1");
	private static final String descr2 = $res("visuals-2");
	private static final String descr3 = $res("visuals-3");
	private static final String descr4 = $res("visuals-4");
	private static final String descr5 = $res("visuals-5");
	private static final String descrS = $res("visuals-selecting");
	private static final String descr = descr1+'\n'+descr2+'\n'+descr3+'\n'+descr4+'\n'+descr5;
	@Override
	public String description() {
		if(posMode == 0) return descr;
		return descrS;
	}

}
