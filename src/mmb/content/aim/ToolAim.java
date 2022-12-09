/**
 * 
 */
package mmbgame.aim;

import static mmbeng.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.menu.wtool.WindowTool;
import mmbeng.block.BlockEntry;
import mmbeng.texture.Textures;

/**
 * @author oskar
 *
 */
public class ToolAim extends WindowTool {
	/**
	 * Creates an aim tool
	 */
	public ToolAim() {
		super("aim");
	}

	private final String title = $res("aim");
	@Override
	public String title() {
		return title;
	}

	public static final ImageIcon icon = new ImageIcon(Textures.get("aim.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}

	@SuppressWarnings("null")
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		if(collector != null) {
			Point chpos = frame.blockPositionOnScreen(mouse.x, mouse.y);
			int s = (int)scale;
			g.drawImage(icon.getImage(), chpos.x, chpos.y, s, s, null);
		}
	}
	@Nonnull private Point mouse = new Point();
	private Aimable collector;
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
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		frame.blockAt(e.getX(), e.getY(), mouse);
	}

	private static final String descr = $res("aim-descr");
	private static final String select = $res("aim-select");
	@Override
	public String description() {
		if(collector == null) 
			return descr;
		return select;
	}

}
