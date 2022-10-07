/**
 * 
 */
package mmb.world.electromachine;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mmb.world.electric.Battery;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 * A helper class to help burn fuels
 */
public class FuelBurner {
	public final double mul;
	public final Inventory inv;
	public final Battery bat;
	public final Object2DoubleMap<ItemEntry> fuels;
	/**
	 * @param mul efficiency multiplier (1 is equal to normal furnace efficiency)
	 * @param inv the inventory to extract fuels from
	 * @param bat the target power output
	 */
	public FuelBurner(double mul, Inventory inv, Battery bat, Object2DoubleMap<ItemEntry> fuels) {
		super();
		this.mul = mul;
		this.inv = inv;
		this.bat = bat;
		this.fuels = fuels;
	}
	/**
	 * Polls the inventory and burns any fuels within
	 */
	public void cycle(){
		for(ItemRecord ir: inv) {
			if(ir.amount() == 0) continue; //The item record is empty
			double fv = fuels.getOrDefault(ir.item(), Double.NaN)*mul; //Fuel energy in joules
			if(Double.isNaN(fv)) continue; //The item is not a fuel
			double remain = bat.remain();
			double fv2 = fv/bat.voltage.volts; //Fuel energy in coulombs
			if(remain < fv2) return; //There is not enough room for the fuel
			int extract = ir.extract(1);
			if(extract == 0) continue; //The extraction failed
			bat.amt += fv2;
		}
	}
	
}
