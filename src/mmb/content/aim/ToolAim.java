/**
 * 
 */
package mmb.content.aim;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.annotations.NN;
import mmb.engine.block.BlockEntry;
import mmb.engine.texture.Textures;
import mmb.menu.wtool.WindowTool;

/**
 * @author oskar
 *
 */
public class ToolAim extends WindowTool {
	/** Creates an aim tool */
	public ToolAim() {
		super("aim");
	}
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		if(collector != null) {
			Point chpos = frame.blockPositionOnScreen(mouse.x, mouse.y);
			int s = (int)scale;
			g.drawImage(icon.getImage(), chpos.x, chpos.y, s, s, null);
		}
	}
	
	//Event listeners
	private Aimable collector;
	@NN private Point mouse = new Point();
	@Override
	public void mouseClicked(MouseEvent e) {
		frame.blockAt(e.getX(), e.getY(), mouse);
		switch(e.getButton()) {
		case 1: //LMB
			if(collector == null) {
				//select a  collector
				BlockEntry block = frame.getMap().get(mouse);
				if(block instanceof Aimable) {
					collector = (Aimable)block;
				}
			}else {
				//Select range
				collector.aimX(mouse.x);
				collector.aimY(mouse.y);
				collector = null;
			}
			
			break;
		case 2: //MMB
			break;
		case 3: //RMB
			collector = null;
			break;
		default:
			break;
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		frame.blockAt(e.getX(), e.getY(), mouse);
	}

	//Description
	/** Description, when no block is selected */
	@NN private static final String DESCR = $res("aim-descr");
	/** Description, when a block is selected */
	@NN private static final String SELECT = $res("aim-select");
	@Override
	public String description() {
		if(collector == null) 
			return DESCR;
		return SELECT;
	}
	
	//Title
	@NN private static final String TITLE = $res("aim");
	@Override
	public String title() {
		return TITLE;
	}
	
	//Icon
	/** The aim icon */
	public static final ImageIcon icon = new ImageIcon(Textures.get("aim.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}
}
