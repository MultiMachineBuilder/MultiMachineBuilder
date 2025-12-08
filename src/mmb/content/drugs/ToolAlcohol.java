/**
 * 
 */
package mmb.content.drugs;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.annotations.NN;
import mmb.engine.chance.Chance;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.texture.Textures;
import mmb.menu.wtool.WindowTool;

/**
 * An item tool associated with alcoholic beverages
 * @author oskar
 */
public class ToolAlcohol extends WindowTool {
	private static final Debugger debug = new Debugger("ALCOHOL");
	
	/** Creates an alcohol tool */
	public ToolAlcohol() {
		super("alcohol");
	}
	@Override
	public void preview(int startX, int startY, double scale, Graphics g) {
		//unused

	}
	
	//Event listeners
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
	
	//Description
	private static final String description = $res("toolalc");
	@Override
	public String description() {
		return description;
	}

	//Title
	private static final String title = $res("alcohol");
	@Override
	public String title() {
		return title;
	}
	
	//Icon	
	@NN public static final Icon ICON = new ImageIcon(Textures.get("liquid/alcohol.png"));
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
