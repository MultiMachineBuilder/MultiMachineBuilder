/**
 * 
 */
package mmb.world.items.electric;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.beans.Electric;
import mmb.world.electric.Battery;
import mmb.world.electric.Electricity;
import mmb.world.electric.VoltageTier;
import mmb.world.item.ItemEntity;
import mmb.world.item.ItemEntityType;
import mmb.world.item.ItemType;
import mmb.world.items.ItemEntry;
import mmb.world.items.pickaxe.Pickaxe;

/**
 * @author oskar
 *
 */
public class ItemBattery extends ItemEntity implements Electric {

	@Nonnull public final Battery battery;
	public ItemBattery(ItemEntityType type, VoltageTier volt) {
		this.type = type;
		battery = new Battery(200, 20000, null, volt);
	}

	@Override
	public ItemEntry itemClone() {
		ItemBattery copy = new ItemBattery(type, battery.voltage);
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

	
	@Nonnull private ItemEntityType type;
	@Override
	public ItemType type() {
		return type;
	}
}