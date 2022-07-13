/**
 * 
 */
package mmb.WORLD.items.electric;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class ItemBattery extends ItemEntity {

	@Nonnull public final Battery battery;
	public ItemBattery(ItemEntityType type, VoltageTier volt) {
		super(type);
		battery = new Battery(200, 20000, null, volt);
	}

	@Override
	public ItemEntry itemClone() {
		ItemBattery copy = new ItemBattery(type(), battery.voltage);
		copy.battery.set(battery);
		return copy;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		battery.load(data);
	}
	@Override
	public JsonNode save() {
		return battery.save();
	}

	@Override
	public void render(Graphics g, int x, int y, int w, int h) {
		super.render(g, x, y, w, h);
		//render durability
		double percent = battery.amt/battery.capacity;
		int min = w/10;
		int max = (w*9)/10;
		if(percent < 0) {
			g.setColor(Color.RED);
			g.drawLine(x+min, y+max, x+max, y+max);
		}else if(percent > 1) {
			g.setColor(Color.CYAN);
			g.drawLine(x+min, y+max, x+max, y+max);
		}else{
			g.setColor(Color.BLACK);
			g.drawLine(x+min, y+max, x+max, y+max);
			int red = 0; 
			int green = 0;
			if(percent > 0.5) {
				green = 255;
				red = (int)(511*(1-percent));
			}else {
				red = 255;
				green = (int)(511*percent);
			}
			Color c = new Color(red, green, 0);
			int scale = (w*8)/10;
			double offset = min+(scale*percent);
			g.setColor(c);
			g.drawLine(x+min, y+max, (int)(x+min+offset), y+max);
		}
	}
}
