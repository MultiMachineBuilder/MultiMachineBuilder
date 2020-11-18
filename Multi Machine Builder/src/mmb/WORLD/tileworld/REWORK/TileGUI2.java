/**
 * 
 */
package mmb.WORLD.tileworld.REWORK;

import java.awt.Graphics;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.events.SharedEventHandler;
import mmb.MENU.toolkit.events.UIMouseEvent;
import mmb.WORLD.tileworld.map.World;

/**
 * @author oskar
 *
 */
public class TileGUI2 extends UIComponent {
	public CommonTileAgent cta;
	@Override
	public SharedEventHandler getCommonEventHandler() {
		return cta;
	}
	
	@Override
	public ComponentEventHandler getHandler() {
		return cta;
	}
	
	public TileGUI2(World w) {
		cta = new CommonTileAgent();
	}

	@Override
	public void prepare(Graphics g) {
		cta.update(g);
	}

	@Override
	public void render(Graphics g) {
		cta.render(g);
	}

	

}
