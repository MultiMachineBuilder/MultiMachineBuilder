/**
 * 
 */
package mmb.content.electric;

import java.awt.Graphics;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.pickaxe.Pickaxe;
import mmb.engine.item.ItemEntityMutable;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;

/**
 * An item which stores electricity. Can be charged and discharged at batteries
 * @author oskar
 */
public class ItemBattery extends ItemEntityMutable implements Electric {
	//Constructors
	/**
	 * Creates an item battery
	 * @param type item type
	 * @param volt voltage tier
	 */
	public ItemBattery(ItemEntityType type, VoltageTier volt) {
		this.type = type;
		battery = new Battery(200, 20000, null, volt);
	}
	//Contents
	@NN private final Battery battery;
	@Override public Battery getElectricity() {
		return battery;
	}
	
	//Item methods
	@NN private ItemEntityType type;
	@Override
	public ItemType type() {
		return type;
	}
	@Override
	public ItemEntry itemClone() {
		ItemBattery copy = new ItemBattery(type, battery.voltage);
		copy.battery.set(battery);
		return copy;
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		battery.load(data);
	}
	@Override
	public JsonNode save() {
		return battery.save();
	}
	
	//Preview
	@Override
	public void render(Graphics g, int x, int y, int w, int h) {
		super.render(g, x, y, w, h);
		Pickaxe.renderDurability(battery.stored/battery.capacity, g, x, y, w, h);
	}
}
