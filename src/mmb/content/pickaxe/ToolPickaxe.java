/**
 * 
 */
package mmb.content.pickaxe;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.annotations.Nil;
import mmb.engine.block.BlockEntry;
import mmb.engine.chance.Chance;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.texture.Textures;
import mmb.menu.wtool.WindowTool;

/**
 * @author oskar
 *
 */
public class ToolPickaxe extends WindowTool {
	@Nil private final Pickaxe pick;
	
	/**
	 * Creates a pickaxe tool bound to a pickaxe
	 * @param pick pickexe to use
	 */
	public ToolPickaxe(@Nil Pickaxe pick) {
		super("pickaxe");
		this.pick = pick;
	}
	/** Creates a creative mode pickaxe tool */
	public ToolPickaxe() {
		this(null);
	}
	
	//Preview
	private static final long toMine = 2_000_000_000L;
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		frame.blockAt(startX+1, startY+1, block);
		if(!frame.getMap().inBounds(block)) return;
		BlockEntry block1 = frame.getMap().get(block);
		if(block1.isSurface()) return;
		if(pressed) {
			int mid = (int)Math.ceil(scale/2);
			int midX = mid+startX;
			int midY = mid+startY;
			long now = System.nanoTime();
			long elapsed = now - lastPressedTime;
			long time = toMine;
			final Pickaxe pick2 = pick;
			if (pick2 != null) 
				time = 20_000_000L * pick2.type().getTime();
			lastPressedTime = now;
			sincePress += elapsed;
			if(sincePress > time) { //block should be mined
				sincePress = time;
				breakBlock(block1);
			}
			int crossscale = (int) (((double)sincePress/time) * mid);
			g.setColor(Color.BLACK);
			g.drawLine(midX, midY, midX-crossscale, midY-crossscale);
			g.drawLine(midX, midY, midX+crossscale, midY-crossscale);
			g.drawLine(midX, midY, midX-crossscale, midY+crossscale);
			g.drawLine(midX, midY, midX+crossscale, midY+crossscale);
		}
	}
	private void breakBlock(BlockEntry block1) {
		Pickaxe pick = null;
		ItemRecord record = window.getPlacer().getSelectedValue();
		if(record != null) {
			ItemEntry entry = record.item();
			if(entry instanceof Pickaxe) {
				pick = (Pickaxe) entry;
				int uses = pick.getUses();
				pick.setUses(uses+1);
				if(uses > pick.type().getDurability()) {
					//Break the pickaxe
					record.extract(Integer.MAX_VALUE);
					pressed = false;
					window.getPlacer().refresh();
					return;
				}
			}
		}
		//Mine the block
		Chance drop = block1.type().getDrop();
		frame.getMap().place(block1.type().leaveBehind(), block.x, block.y);
		drop.drop(window.getPlayer().inv.createWriter(), frame.getMap(), block.x, block.y);
		
		if(record != null && pick != null && pick.getUses() > pick.type().getDurability()) {
			//Break the pickaxe
			record.extract(Integer.MAX_VALUE);
		}
		
		window.getPlacer().refresh();
		//Unpress the button
		pressed = false;
	}
	
	//Event listeners
	private boolean pressed;
	private long lastPressedTime;
	private long sincePress;
	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		lastPressedTime = System.nanoTime();
		sincePress = 0;
		frame.blockAt(e.getX(), e.getY(), block);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
	}
	@Override
	public void deselected() {
		pressed = false;
	}
	private Point block = new Point();
	private Point block0 = new Point();	
	@Override
	public void mouseMoved(MouseEvent e) {
		block0.setLocation(block);
		frame.blockAt(e.getX(), e.getY(), block);
		if(!block.equals(block0)) {
			//Moved
			sincePress = 0;
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	//Title	
	private final String title = $res("pickaxe");
	@Override
	public String title() {
		return title;
	}

	//Icon
	public static final Icon icon = new ImageIcon(Textures.get("base/pickaxe.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}
}
