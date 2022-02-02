/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Point;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.Drop;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.pickaxe.Pickaxe;
import mmb.WORLD.items.pickaxe.Pickaxe.PickaxeType;

/**
 * @author oskar
 *
 */
public class ToolPickaxe extends WindowTool {
	//Mock pickaxe for testing.
	@Nonnull private static PickaxeType mockType = new PickaxeType().setDurability(Integer.MAX_VALUE);
	@Nonnull private static Pickaxe pick0 = (Pickaxe) mockType.create();
	
	@Nullable private final Pickaxe pick;
	public ToolPickaxe(@Nullable Pickaxe pick) {
		super("pickaxe");
		this.pick = pick;
	}
	public ToolPickaxe() {
		this(null);
	}

	@Override
	public String title() {
		return "Pickaxe";
	}

	public static final Icon icon = new ImageIcon(Textures.get("base/pickaxe.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	private static final long toMine = 2_000_000_000L;

	@Override
	public void preview(int startX, int startY, int scale, Graphics g) {
		frame.blockAt(startX+1, startY+1, block);
		if(!frame.getMap().inBounds(block)) return;
		BlockEntry block1 = frame.getMap().get(block);
		if(block1.isSurface()) return;
		if(pressed) {
			int mid = scale/2;
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
				if(uses > pick.durability) {
					//Break the pickaxe
					record.extract(Integer.MAX_VALUE);
					pressed = false;
					window.getPlacer().refresh();
					return;
				}
			}
		}
		//Mine the block
		Drop drop = block1.type().getDrop();
		frame.getMap().reserveAndDo(block.x, block.y, slot -> {
			BlockEntry changed = slot.set(block1.type().leaveBehind().createBlock());
		});
		boolean hasDrops = drop.drop(window.getPlayer().inv.createWriter(), frame.getMap(), block.x, block.y);
		
		if(record != null && pick != null && pick.getUses() > pick.durability) {
			//Break the pickaxe
			record.extract(Integer.MAX_VALUE);
		}
		
		window.getPlacer().refresh();
		//Unpress the button
		pressed = false;
	}
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
}
