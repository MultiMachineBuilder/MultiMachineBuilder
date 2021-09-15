/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.gui.window.WorldFrame;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class Copy extends WindowTool {

	private int state = 0;
	/**
	 * @param s
	 */
	protected Copy() {
		super("Copy");
	}

	@Override
	public String description() {
		switch(state) {
		default:
			state = 0;
			//$FALL-THROUGH$
		case 0:
			return "Pick first corner with LMB";
		case 1:
			return "Pick second corner, press LMB to copy, press MMB to cut, or press RMB to cancel";
		case 2:
			return "Pick location to paste with LMB, or press RMB to delete selection";
		}
	}
	
	private Grid<BlockEntry> grid;
	private Point a, b;
	private int boxsizeX, boxsizeY;
	private Point mouse;
	private static final Debugger debug = new Debugger("TOOL-COPY");

	@Override public Icon getIcon() {
		return icon;
	}
	
	public static final Icon icon = new ImageIcon(Textures.get("copy.png"));
	@Override
	public String title() {
		return "Copy";
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch(state) {
		default:
			state = 0;
			//$FALL-THROUGH$
		case 0:
			switch(e.getButton()) {
			case 1: //LMB
				a = frame.blockAt(e.getX(), e.getY());
				state = 1;
			}
			break;
		case 1:
			switch(e.getButton()) {
			case 1: //LMB
				b = frame.blockAt(e.getX(), e.getY());
				//Sort coordinates
				if(a.x > b.x) {
					int tmp = a.x;
					a.x = b.x;
					b.x = tmp;
				}
				if(a.y > b.y) {
					int tmp = a.y;
					a.y = b.y;
					b.y = tmp;
				}
				boxsizeX = (b.x - a.x) + 1;
				boxsizeY = (b.y - a.y) + 1;
				//Copy the data
				World world = frame.getMap();
				grid = new FixedGrid<>(boxsizeX, boxsizeY);
				for(int i = 0, x = a.x; x <= b.x; i++, x++) {
					debug.printl("Line");
					for(int j = 0, y = a.y; y <= b.y; j++, y++) {
						debug.printl("Copy"); //FIXME does not run
						grid.set(i, j, world.get(x, y));
					}
				}
				state = 2;
				break;
			case 2: //MMB
				break;
			case 3: //RMB
				a = null;
				state = 0;
			}
			break;
		case 2:
			switch(e.getButton()) {
			case 1: //LMB
				//New B corner
				Point c = frame.blockAt(e.getX(), e.getY());
				int shiftX = c.x - b.x;
				int shiftY = c.y - b.y;
				//New A cornar
				int dx = a.x + shiftX;
				int dy = a.y + shiftY;
				World world = frame.getMap();
				for(int i = 0, x = dx; x <= c.x; i++, x++) {
					for(int j = 0, y = dy; y <= c.y; j++, y++) {
						BlockEntry toPlace = grid.get(i, j);
						BlockEntry copy = toPlace.blockCopy();
						world.set(copy, x, y);
					}
				}
				break;
			case 2: //MMB
				///break;
			case 3: //RMB
				a = null;
				b = null;
				grid = null;
				state = 0;
			}
		}
		
	}

	@Override
	public void preview(int x, int y, int scale, Graphics g) {
		if(a == null) {
			WorldFrame.thickframe(x, y, scale-1, scale-1, Color.RED, g);
		}else {
			if(b == null) {
				Point onmouse = frame.blockAt(mouse);
				frame.renderBlockRange(onmouse.x, onmouse.y, a.x, a.y, Color.ORANGE, g);
			}else {//Draw previewed blocks
				//Calculate positions
				int sizex = (grid.width())*scale;
				int sizey = (grid.height())*scale;
				int sizeX = sizex-scale;
				int sizeY = sizey-scale;
				int startX = x-sizeX;
				int startY = y-sizeY;
				
				Point p = new Point();
				for(int px = startX, i = 0; i < grid.width(); px += scale, i++) {
					for(int py = startY, j = 0; j < grid.height(); py += scale, j++) {
						BlockEntry todraw = grid.get(i, j);
						todraw.render(px, py, g, scale);
					}
				}
				
				//Draw frame
				WorldFrame.thickframe(startX, startY, sizex-1, sizey-1, Color.GREEN, g);
				
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = e.getPoint();
	}
}
