/**
 * 
 */
package mmb.content.imachine.extractor;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.Nil;
import mmb.beans.Resizable;
import mmb.engine.block.BlockEntry;
import mmb.engine.texture.Textures;
import mmb.menu.wtool.WindowTool;

/**
 * Sets the range of dropped item exractors
 * @author oskar
 */
public class ConfigureDroppedItemExtractors extends WindowTool {
	/** Creates an extrector configurer */
	public ConfigureDroppedItemExtractors() {
		super("droppedItems");
	}
	@SuppressWarnings("null")
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		if(collector != null) {
			int size = collector.maxSize();
			int rangeX = BlockCollector.clamp(1, mouse.x-collector.posX(), size);
			int rangeY = BlockCollector.clamp(1, mouse.y-collector.posY(), size);
			frame.renderBlockRange(collector.posX(), collector.posY(), collector.posX()+collector.getRangeX()-1, collector.posY()+collector.getRangeY()-1, Color.BLUE, g);
			frame.renderBlockRange(collector.posX(), collector.posY(), collector.posX()+rangeX-1, collector.posY()+rangeY-1, Color.ORANGE, g);
		}
	}

	//Event listeners
	private Point mouse = new Point();
	@Nil private Resizable collector;
	@Override
	public void mouseClicked(MouseEvent e) {
		frame.blockAt(e.getX(), e.getY(), mouse);
		switch(e.getButton()) {
		case 1: //LMB
			if(collector == null) {
				//select a  collector
				BlockEntry block = frame.getMap().get(mouse);
				if(block instanceof Resizable) {
					collector = (Resizable)block;
				}
			}else {
				//Select range
				int size = collector.maxSize();
				int rangeX = BlockCollector.clamp(1, mouse.x-collector.posX(), size);
				int rangeY = BlockCollector.clamp(1, mouse.y-collector.posY(), size);
				collector.setRangeX(rangeX);
				collector.setRangeY(rangeY);
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
	
	//Description
	private static final String descr = $res("cdie-descr");
	private static final String select = $res("cdie-select");
	@Override
	public String description() {
		if(collector == null) 
			return descr;
		return select;
	}
	
	//Title
	private final String title = $res("cdie");
	@Override
	public String title() {
		return title;
	}

	//Icon
	public static final Icon icon = new ImageIcon(Textures.get("hoover.png"));
	@Override
	public Icon getIcon() {
		return icon;
	}
}
