/**
 * 
 */
package mmb.menu.wtool;

import static mmb.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.beans.Intoxicating;
import mmb.debug.Debugger;
import mmb.texture.Textures;
import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.ItemRecord;
import mmb.world.item.ItemEntry;

/**
 * @author oskar
 *
 */
public class ToolAlcohol extends WindowTool {
	@Nonnull public static final Icon ICON = new ImageIcon(Textures.get("liquid/alcohol.png"));
	private static final String title = $res("alcohol");
	private static final String description = $res("toolalc");
	private static final Debugger debug = new Debugger("ALCOHOL");
	public ToolAlcohol() {
		super("alcohol");
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public Icon getIcon() {
		return ICON;
	}

	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		//unused

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() != MouseEvent.BUTTON1) return; //Wrong button
		//Drink alcohol
		ItemRecord irecord = window.getPlacer().getSelectedValue();
		if(irecord == null) return;
		ItemEntry item = irecord.item();
		if(item instanceof Intoxicating) {
			//If in survival, consume items
			if(window.getPlayer().isSurvival()) {
				//In survival
				int extracted = irecord.extract(1);
				if(extracted == 0) return;
				Chance drop = ((Intoxicating) item).postdrink();
				drop.produceResults(frame.getPlayer().inv.createWriter());
			}
			double dose = ((Intoxicating) item).alcoholicity();	
			debug.printl("Drink! "+dose);
			frame.getPlayer().drinkAlcohol(dose);
			((Intoxicating) item).effects();
		}
	}

}
