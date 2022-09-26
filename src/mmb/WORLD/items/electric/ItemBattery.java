/**
 * 
 */
package mmb.WORLD.items.electric;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Electric;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.pickaxe.Pickaxe;

/**
 * @author oskar
 *
 */
public class ItemBattery extends ItemEntity implements Electric {

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
	protected int hash0() {
		return battery.hashCode();
	}

	@Override
	protected boolean equal0(ItemEntity other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public Electricity getElectricity() {
		return battery;
	}
	
	@Override
	public void render(Graphics g, int x, int y, int w, int h) {
		super.render(g, x, y, w, h);
		Pickaxe.renderDurability(battery.amt/battery.capacity, g, x, y, w, h);
	}
}
